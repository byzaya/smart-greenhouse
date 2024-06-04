import telebot
from telebot import types
import datetime
import requests
import bcrypt
import subprocess
import threading
import re
import os
import argparse
import psycopg2
import uuid
import requests
from threading import Timer


TOKEN = "TOKEN"
hashed_password = b'$2a$10$P0nV556yVnGe2xvHKiEsuOCI.QwsLu0UmmWuDAChkxEsFRYqS50Ou'
active_tunnel_subdomain = None
authorized_users = set()
greenhouses_info = {}

user_step = {}
bot = telebot.TeleBot(TOKEN)

parser = argparse.ArgumentParser(description='Запуск Telegram-бота с JWT-токеном')
parser.add_argument('--token-file', type=str, required=True, help='Путь к файлу, содержащему JWT-токен')
args = parser.parse_args()

def load_jwt_token(token_file_path):
    if not os.path.exists(token_file_path):
        raise FileNotFoundError(f"Файл токена не найден по пути: {token_file_path}")

    if os.path.getsize(token_file_path) == 0:
        raise Exception("Файл токена пуст.")

    try:
        with open(token_file_path, 'r') as file:
            jwt_token = file.read().strip()
            if not jwt_token:
                raise ValueError("Файл токена пуст после удаления пробелов.")
            return jwt_token
    except Exception as e:
        raise Exception(f"Ошибка при чтении файла: {e}")

jwt_token = load_jwt_token(args.token_file)

def connect_db():
    try:
        connection = psycopg2.connect(
            host='localhost',
            port='5433',
            database='greenhouse',
            user='admin',
            password='admin')
        return connection
    except Exception as e:
        print(f"Ошибка подключения к базе данных: {e}")
        return None

seedbed_data = {}

def create_unique_identifier():
    return str(uuid.uuid4())

def store_seedbed_data(identifier, data):
    seedbed_data[identifier] = data

def get_seedbed_data(identifier):
    return seedbed_data.get(identifier)

def remove_seedbed_data(identifier):
    if identifier in seedbed_data:
        del seedbed_data[identifier]

def format_status_message(temperature):
    if 16 <= temperature <= 28:
        status_icon = "🟢"
    else:
        status_icon = "🔴"

    current_time = datetime.datetime.now().strftime("%H:%M:%S")
    status_message = f"Статус: {status_icon}\nТемпература: {temperature}°C\nВремя: {current_time}"
    return status_message


@bot.message_handler(commands=['start'])
def send_welcome(message):
    user_id = message.from_user.id
    if user_id not in user_step or user_step[user_id] != 'authorized':
        markup = types.ReplyKeyboardRemove(selective=False)
        bot.send_message(message.chat.id, "Введите код доступа", reply_markup=markup)
        user_step[user_id] = 'authenticating'
    else:
        send_main_menu(message.chat.id)


@bot.message_handler(func=lambda message: user_step.get(message.from_user.id) == 'authenticating')
def authentication(message):
    user_id = message.from_user.id
    bot.delete_message(message.chat.id, message.message_id)
    if bcrypt.checkpw(message.text.encode('utf-8'), hashed_password):
        user_step[user_id] = 'authorized'
        authorized_users.add(user_id)
        send_main_menu(message.chat.id)


def send_main_menu(chat_id, message_id=None):
    markup = types.InlineKeyboardMarkup()
    
    btn1 = types.InlineKeyboardButton("🏡 Управление теплицами", callback_data='greenhouse_info')
    btn2 = types.InlineKeyboardButton("🌐 Управление туннелированием", callback_data='tunnel_management')
    btn4 = types.InlineKeyboardButton("⚙️ Перезагрузить устройство", callback_data='restart_device')
    
    markup.add(btn1)
    markup.add(btn2)
    markup.add(btn4)
    
    main_menu_text = "*🏠 Главное меню*"
    
    if message_id:
        bot.edit_message_text(main_menu_text, chat_id=chat_id, message_id=message_id, reply_markup=markup, parse_mode='Markdown')
    else:
        bot.send_message(chat_id, main_menu_text, reply_markup=markup, parse_mode='Markdown')

def get_greenhouses():
    connection = connect_db()
    if connection is not None:
        try:
            cursor = connection.cursor()
            cursor.execute("SELECT id, greenhouse_name FROM greenhouse;")
            greenhouses = cursor.fetchall()
            cursor.close()
            connection.close()
            global greenhouses_info
            greenhouses_info = {str(gh[0]): gh[1] for gh in greenhouses}
            return list(greenhouses_info.items())
        except Exception as e:
            print(f"Ошибка при выполнении запроса к базе данных: {e}")
            return []
    else:
        return []

def send_greenhouse_menu(chat_id, message_id=None):
    greenhouse_names = get_greenhouses()
    markup = types.InlineKeyboardMarkup()
    for greenhouse_id, name in greenhouse_names:
        btn = types.InlineKeyboardButton(name, callback_data=f"greenhouse_data:{greenhouse_id}")
        markup.add(btn)
    btn_back = types.InlineKeyboardButton("Меню", callback_data='main_menu')
    markup.add(btn_back)
    if message_id:
        bot.edit_message_text("Выберите теплицу:", chat_id=chat_id, message_id=message_id, reply_markup=markup)
    else:
        bot.send_message(chat_id, "Выберите теплицу:", reply_markup=markup)


def send_tunnel_management_menu(chat_id, message_id=None):
    tunnel_active = is_any_tunnel_active()
    markup = types.InlineKeyboardMarkup()
    btn_server_info = types.InlineKeyboardButton("🛰️ Информация о сервере", callback_data='server_info')
    btn_back = types.InlineKeyboardButton("Меню", callback_data='main_menu')
    markup.add(btn_server_info)
    if not tunnel_active:
        btn_activate_tunnel = types.InlineKeyboardButton("🚀 Активация туннеля", callback_data='raise_tunnel')
        markup.add(btn_activate_tunnel)
    markup.add(btn_back)
    bot.edit_message_text("Управление туннелированием", chat_id=chat_id, message_id=message_id, reply_markup=markup)


def send_specific_greenhouse_data(chat_id, greenhouse_id, message_id):
    connection = connect_db()
    if connection is not None:
        try:
            cursor = connection.cursor()
            greenhouse_name = greenhouses_info.get(greenhouse_id, "Неизвестная теплица")

            cursor.execute("SELECT location FROM greenhouse WHERE id = %s;", (greenhouse_id,))
            location = cursor.fetchone()[0]

            markup = types.InlineKeyboardMarkup()

            cursor.execute("SELECT max_temperature, min_temperature, max_light, min_light FROM configurations WHERE greenhouse_id = %s;", (greenhouse_id,))
            config = cursor.fetchone()
            if config:
                max_temp, min_temp, max_light, min_light = config
            else:
                max_temp, min_temp, max_light, min_light = None, None, None, None

            cursor.execute("SELECT id, is_active FROM configurations WHERE greenhouse_id = %s;", (greenhouse_id,))
            config_id = cursor.fetchone()[0]

            btn_text = f"⚙️ Конфигурация"
            btn = types.InlineKeyboardButton(btn_text, callback_data=f"config_detail:{config_id}:{greenhouse_id}")
            markup.add(btn)

            btn_add_seedbed = types.InlineKeyboardButton("➕ Добавить грядку", callback_data=f"add_seedbed:{greenhouse_id}")
            markup.add(btn_add_seedbed)

            cursor.execute("SELECT id, seedbed_name FROM seedbeds WHERE greenhouse_id = %s;", (greenhouse_id,))
            seedbeds = cursor.fetchall()

            for seedbed_id, seedbed_name in seedbeds:
                button = types.InlineKeyboardButton(f"🌱 Грядка: {seedbed_name}", callback_data=f"seedbed_info:{seedbed_id}:{greenhouse_id}")
                markup.add(button)
           
            cursor.execute("""
                WITH LatestTemperature AS (
                    SELECT
                        t.sensor_id,
                        t.value,
                        t.receive_time,
                        ROW_NUMBER() OVER (PARTITION BY t.sensor_id ORDER BY t.receive_time DESC) as rn
                    FROM temperature t
                    JOIN sensors s ON t.sensor_id = s.id
                    WHERE s.greenhouse_id = %s AND s.is_active = TRUE
                )
                SELECT AVG(t.value) AS average_temperature
                FROM LatestTemperature t
                WHERE t.rn = 1;
            """, (greenhouse_id,))
            
            result_temp = cursor.fetchone()
            average_temperature = result_temp[0]
            
            if average_temperature is not None and min_temp is not None and max_temp is not None:
                if min_temp <= average_temperature <= max_temp:
                    temp_status = '🟢'
                else:
                    temp_status = '🔴'
                temp_text = f"{average_temperature:.2f}°C"
            else:
                temp_status = '⚪'
                temp_text = "Нет данных"

            cursor.execute("""
                WITH LatestLight AS (
                    SELECT
                        l.sensor_id,
                        l.value,
                        l.receive_time,
                        ROW_NUMBER() OVER (PARTITION BY l.sensor_id ORDER BY l.receive_time DESC) as rn
                    FROM light l
                    JOIN sensors s ON l.sensor_id = s.id
                    WHERE s.greenhouse_id = %s AND s.is_active = TRUE
                )
                SELECT AVG(l.value) AS average_light
                FROM LatestLight l
                WHERE l.rn = 1;
            """, (greenhouse_id,))

            result_light = cursor.fetchone()
            average_light = result_light[0]

            if average_light is not None and min_light is not None and max_light is not None:
                if min_light <= average_light <= max_light:
                    light_status = '🟢'
                else:
                    light_status = '🔴'
                light_text = f"{average_light:.2f} lx"
            else:
                light_status = '⚪'
                light_text = "Нет данных"
            
            message_text = (
                f"Теплица: {greenhouse_name}\n"
                f"Локация: {location}\n"
                f"Количество грядок: {len(seedbeds)}\n"
                f"{temp_status} Средняя температура: {temp_text}\n"
                f"{light_status} Средняя освещенность: {light_text}\n"
            )

            markup.add(types.InlineKeyboardButton("◀️ Назад", callback_data='greenhouse_info'))

            bot.edit_message_text(text=message_text, chat_id=chat_id, message_id=message_id, reply_markup=markup)
            cursor.close()
        except Exception as e:
            print(f"Ошибка при выполнении запроса к базе данных: {e}")
        finally:
            cursor.close()
            connection.close()

def send_specific_seedbed_data(chat_id, seedbed_id, message_id, greenhouse_id):
    connection = connect_db()
    if connection is not None:
        try:
            cursor = connection.cursor()
            cursor.execute("SELECT is_auto, max_humidity, min_humidity, watering_duration, watering_enabled, watering_frequency, seedbed_name FROM seedbeds WHERE id = %s;", (seedbed_id,))
            seedbed_info = cursor.fetchone()
            is_auto, max_humidity, min_humidity, watering_duration, watering_enabled, watering_frequency, seedbed_name = seedbed_info
            mode = "Включен" if is_auto else "Выключен"
            watering = "Включен" if watering_enabled else "Выключен"

            cursor.execute("""
                WITH LatestHumidity AS (
                    SELECT
                        h.sensor_id,
                        h.value,
                        h.receive_time,
                        ROW_NUMBER() OVER (PARTITION BY h.sensor_id ORDER BY h.receive_time DESC) as rn
                    FROM humidity h
                    JOIN sensors s ON h.sensor_id = s.id
                    WHERE s.greenhouse_id = %s AND s.is_active = TRUE
                )
                SELECT AVG(h.value) AS average_humidity
                FROM LatestHumidity h
                WHERE h.rn = 1;
            """, (greenhouse_id,))
            result = cursor.fetchone()
            average_humidity = result[0]

            if average_humidity is not None:
                if min_humidity <= average_humidity <= max_humidity:
                    humidity_status = '🟢'
                else:
                    humidity_status = '🔴'
                humidity_text = f"{average_humidity:.2f}%"
            else:
                humidity_status = '⚪'
                humidity_text = "Нет данных"

            message_text = (
                f"Грядка: {seedbed_name}\n"
                f"Автономный режим: {mode}\n"
                f"Полив: {watering}\n"
                f"Длительность полива: {watering_duration}\n"
                f"Частота полива: {watering_frequency}\n"
                f"Макс. влажность: {max_humidity}\n"
                f"Мин. влажность: {min_humidity}\n"
                f"{humidity_status} Средняя влажность: {humidity_text}\n"
            )
            markup = types.InlineKeyboardMarkup()

            mode_btn_text = "🟢 Автономное упр." if mode else "🔴 Автономное упр."
            toggle_mode_btn = types.InlineKeyboardButton(mode_btn_text, callback_data=f"toggle_mode:{seedbed_id}:{greenhouse_id}")
            markup.add(toggle_mode_btn)

            edit_name_btn = types.InlineKeyboardButton("✏️ Изменить название грядки", callback_data=f"edit_name:{seedbed_id}:{greenhouse_id}")
            markup.add(edit_name_btn)

            edit_watering_duration_btn = types.InlineKeyboardButton("🔢 Изменить длительность полива", callback_data=f"edit_watering_duration:{seedbed_id}:{greenhouse_id}")
            markup.add(edit_watering_duration_btn)

            edit_watering_frequency_btn = types.InlineKeyboardButton("🔢 Изменить частоту полива", callback_data=f"edit_watering_frequency:{seedbed_id}:{greenhouse_id}")
            markup.add(edit_watering_frequency_btn)

            edit_humidity_btn = types.InlineKeyboardButton("🔢 Изменить пределы влажности", callback_data=f"edit_humidity:{seedbed_id}:{greenhouse_id}")
            markup.add(edit_humidity_btn)

            delete_seedbed_btn = types.InlineKeyboardButton("🗑️ Удалить грядку", callback_data=f"delete_seedbed:{seedbed_id}:{greenhouse_id}")
            markup.add(delete_seedbed_btn)

            back_btn = types.InlineKeyboardButton("◀️ Назад", callback_data=f"greenhouse_data:{greenhouse_id}")
            markup.add(back_btn)

            bot.edit_message_text(text=message_text, chat_id=chat_id, message_id=message_id, reply_markup=markup)
            cursor.close()
        except Exception as e:
            print(f"Ошибка при выполнении запроса к базе данных: {e}")
        finally:
            connection.close()

def send_configurations_menu(chat_id, greenhouse_id, message_id):
    connection = connect_db()
    if connection is not None:
        try:
            cursor = connection.cursor()
            cursor.execute("SELECT id, is_active FROM configurations WHERE greenhouse_id = %s ORDER BY id;", (greenhouse_id,))
            configurations = cursor.fetchall()

            markup = types.InlineKeyboardMarkup()
            for index, (config_id, is_active) in enumerate(configurations, start=1):
                active_icon = "✅" if is_active else ""
                btn_text = f"⚙️ Конфигурация {index} {active_icon}"
                btn = types.InlineKeyboardButton(btn_text, callback_data=f"config_detail:{config_id}:{greenhouse_id}")
                markup.add(btn)

            btn_back = types.InlineKeyboardButton("◀️ Назад", callback_data=f"greenhouse_data:{greenhouse_id}")
            markup.add(btn_back)

            bot.edit_message_text(text="Конфигурации теплицы:", chat_id=chat_id, message_id=message_id, reply_markup=markup)
        finally:
            cursor.close()
            connection.close()

def send_configuration_detail(chat_id, config_id, greenhouse_id, message_id):
    connection = connect_db()
    if connection is not None:
        try:
            cursor = connection.cursor()
            cursor.execute("SELECT is_active, is_auto, max_temperature, min_temperature, max_light, min_light FROM configurations WHERE id = %s;", (config_id,))
            config = cursor.fetchone()
            if config:
                is_active, is_auto, max_temp, min_temp, max_light, min_light = config
                active_status = "Активна" if is_active else "Неактивна"
                auto_status = "Включен" if is_auto else "Выключен"
                message_text = f"Информация о конфигурации:\nСтатус: {active_status}\nАвтономный режим: {auto_status}\nМакс. темп.: {max_temp}°C\nМин. темп.: {min_temp}°C\nМакс. освещённость: {max_light} lx\nМин. освещённость: {min_light} lx"

                markup = types.InlineKeyboardMarkup()
                toggle_active_btn = types.InlineKeyboardButton("🔄 Выключить" if is_active else "🔄 Включить", callback_data=f"toggle_active:{config_id}:{greenhouse_id}")
                markup.add(toggle_active_btn)
                toggle_auto_btn = types.InlineKeyboardButton("🟢 Автономное упр." if is_auto else "🔴 Автономное упр.", callback_data=f"toggle_auto:{config_id}:{greenhouse_id}")
                markup.add(toggle_auto_btn)
                edit_temp_btn = types.InlineKeyboardButton("🔢 Изменить пределы температуры", callback_data=f"edit_temp:{config_id}:{greenhouse_id}")
                markup.add(edit_temp_btn)
                edit_light_btn = types.InlineKeyboardButton("🔢 Изменить пределы освещённости", callback_data=f"edit_light:{config_id}:{greenhouse_id}")
                markup.add(edit_light_btn)
                back_btn = types.InlineKeyboardButton("◀️ Назад", callback_data=f"greenhouse_data:{greenhouse_id}")
                markup.add(back_btn)
            else:
                message_text = "Информация о конфигурации не найдена."
                markup = types.InlineKeyboardMarkup().add(types.InlineKeyboardButton("◀️ Назад", callback_data=f"greenhouse_data:{greenhouse_id}"))
            
            bot.edit_message_text(text=message_text, chat_id=chat_id, message_id=message_id, reply_markup=markup)
        finally:
            cursor.close()
            connection.close()

def toggle_mode(call):
    _, seedbed_id, greenhouse_id = call.data.split(':')
    connection = connect_db()
    if connection is not None:
        try:
            cursor = connection.cursor()
            cursor.execute("SELECT is_auto FROM seedbeds WHERE id = %s;", (seedbed_id,))
            is_auto = cursor.fetchone()[0]
            new_mode = not is_auto
            cursor.execute("UPDATE seedbeds SET is_auto = %s WHERE id = %s;", (new_mode, seedbed_id))
            connection.commit()
            bot.answer_callback_query(call.id, "Режим изменён.")
            send_specific_seedbed_data(call.message.chat.id, seedbed_id, call.message.message_id, greenhouse_id)
        except Exception as e:
            print(f"Ошибка: {str(e)}")
        finally:
            cursor.close()
            connection.close()

@bot.callback_query_handler(func=lambda call: call.data.startswith("toggle_watering:"))
def toggle_watering(call):
    _, seedbed_id, greenhouse_id = call.data.split(':')
    connection = connect_db()
    if connection is not None:
        try:
            cursor = connection.cursor()
            cursor.execute("SELECT watering_enabled FROM seedbeds WHERE id = %s;", (seedbed_id,))
            watering_enabled = cursor.fetchone()[0]
            new_status = not watering_enabled
            cursor.execute("UPDATE seedbeds SET watering_enabled = %s WHERE id = %s;", (new_status, seedbed_id))
            connection.commit()
            bot.answer_callback_query(call.id, "Статус полива изменён.")
            send_specific_seedbed_data(call.message.chat.id, seedbed_id, call.message.message_id, greenhouse_id)
        except Exception as e:
            print(f"Ошибка: {str(e)}")
        finally:
            cursor.close()
            connection.close()

def edit_name(call):
    _, seedbed_id, greenhouse_id = call.data.split(':')
    markup = types.InlineKeyboardMarkup()
    back_btn = types.InlineKeyboardButton("◀️ Назад", callback_data=f"seedbed_info:{seedbed_id}:{greenhouse_id}")
    markup.add(back_btn)
    bot.edit_message_text("Введите новое название грядки (1-50 символов):",
                          chat_id=call.message.chat.id,
                          message_id=call.message.message_id,
                          reply_markup=markup)
    bot.register_next_step_handler_by_chat_id(call.message.chat.id, process_name_input, seedbed_id, greenhouse_id, call.message.message_id)

def process_name_input(message, seedbed_id, greenhouse_id, message_id):
    new_name = message.text.strip()
    bot.delete_message(message.chat.id, message.message_id)
    if 0 < len(new_name) <= 50:
        connection = connect_db()
        if connection is not None:
            try:
                cursor = connection.cursor()
                cursor.execute("UPDATE seedbeds SET seedbed_name = %s WHERE id = %s;", (new_name, seedbed_id))
                connection.commit()
                send_specific_seedbed_data(message.chat.id, seedbed_id, message_id, greenhouse_id)
            finally:
                cursor.close()
                connection.close()
    else:
        error_text = "Ошибка: Название грядки должно быть от 1 до 50 символов."
        markup = types.InlineKeyboardMarkup()
        back_btn = types.InlineKeyboardButton("◀️ Назад", callback_data=f"seedbed_info:{seedbed_id}:{greenhouse_id}")
        markup.add(back_btn)
        bot.edit_message_text(chat_id=message.chat.id, message_id=message_id,
                              text=error_text,
                              reply_markup=markup)


def edit_watering_duration(call):
    _, seedbed_id, greenhouse_id = call.data.split(':')
    markup = types.InlineKeyboardMarkup()
    back_btn = types.InlineKeyboardButton("◀️ Назад", callback_data=f"seedbed_info:{seedbed_id}:{greenhouse_id}")
    markup.add(back_btn)
    bot.edit_message_text("Введите новую длительность полива (положительное целое число):",
                          chat_id=call.message.chat.id,
                          message_id=call.message.message_id,
                          reply_markup=markup)
    bot.register_next_step_handler_by_chat_id(call.message.chat.id, process_existing_watering_duration_input, seedbed_id, greenhouse_id, call.message.message_id)

def process_existing_watering_duration_input(message, seedbed_id, greenhouse_id, message_id):
    text = message.text.strip()
    bot.delete_message(message.chat.id, message.message_id)
    if text.isdigit() and int(text) > 0:
        new_duration = int(text)
        connection = connect_db()
        if connection is not None:
            try:
                cursor = connection.cursor()
                cursor.execute("UPDATE seedbeds SET watering_duration = %s WHERE id = %s;", (new_duration, seedbed_id))
                connection.commit()
                send_specific_seedbed_data(message.chat.id, seedbed_id, message_id, greenhouse_id)
            finally:
                cursor.close()
                connection.close()
    else:
        error_text = "Ошибка: Укажите положительное целое число для длительности полива."
        markup = types.InlineKeyboardMarkup()
        back_btn = types.InlineKeyboardButton("◀️ Назад", callback_data=f"seedbed_info:{seedbed_id}:{greenhouse_id}")
        markup.add(back_btn)
        bot.edit_message_text(chat_id=message.chat.id, message_id=message_id,
                              text=error_text,
                              reply_markup=markup)


def process_watering_duration_input(message, seedbed_id, greenhouse_id, message_id):
    text = message.text.strip()
    bot.delete_message(message.chat.id, message.message_id)
    if text.isdigit() and int(text) > 0:
        new_duration = int(text)
        connection = connect_db()
        if connection is not None:
            try:
                cursor = connection.cursor()
                cursor.execute("UPDATE seedbeds SET watering_duration = %s WHERE id = %s;", (new_duration, seedbed_id))
                connection.commit()
                send_specific_seedbed_data(message.chat.id, seedbed_id, message_id, greenhouse_id)
            finally:
                cursor.close()
                connection.close()
    else:
        error_text = "Ошибка: Укажите положительное целое число для длительности полива."
        markup = types.InlineKeyboardMarkup()
        back_btn = types.InlineKeyboardButton("◀️ Назад", callback_data=f"seedbed_info:{seedbed_id}:{greenhouse_id}")
        markup.add(back_btn)
        bot.edit_message_text(chat_id=message.chat.id, message_id=message_id,
                              text=error_text,
                              reply_markup=markup)


def edit_watering_frequency(call):
    _, seedbed_id, greenhouse_id = call.data.split(':')
    markup = types.InlineKeyboardMarkup()
    back_btn = types.InlineKeyboardButton("◀️ Назад", callback_data=f"seedbed_info:{seedbed_id}:{greenhouse_id}")
    markup.add(back_btn)
    bot.edit_message_text("Введите новую частоту полива (положительное целое число):",
                          chat_id=call.message.chat.id,
                          message_id=call.message.message_id,
                          reply_markup=markup)
    bot.register_next_step_handler_by_chat_id(call.message.chat.id, process_existing_watering_frequency_input, seedbed_id, greenhouse_id, call.message.message_id)

def process_existing_watering_frequency_input(message, seedbed_id, greenhouse_id, message_id):
    text = message.text.strip()
    bot.delete_message(message.chat.id, message.message_id)
    if text.isdigit() and int(text) > 0:
        new_frequency = int(text)
        connection = connect_db()
        if connection is not None:
            try:
                cursor = connection.cursor()
                cursor.execute("UPDATE seedbeds SET watering_frequency = %s WHERE id = %s;", (new_frequency, seedbed_id))
                connection.commit()
                send_specific_seedbed_data(message.chat.id, seedbed_id, message_id, greenhouse_id)
            finally:
                cursor.close()
                connection.close()
    else:
        error_text = "Ошибка: Укажите положительное целое число для частоты полива."
        markup = types.InlineKeyboardMarkup()
        back_btn = types.InlineKeyboardButton("◀️ Назад", callback_data=f"seedbed_info:{seedbed_id}:{greenhouse_id}")
        markup.add(back_btn)
        bot.edit_message_text(chat_id=message.chat.id, message_id=message_id,
                              text=error_text,
                              reply_markup=markup)



def process_watering_frequency_input(message, seedbed_id, greenhouse_id, message_id):
    text = message.text.strip()
    bot.delete_message(message.chat.id, message.message_id)
    if text.isdigit() and int(text) > 0:
        new_frequency = int(text)
        connection = connect_db()
        if connection is not None:
            try:
                cursor = connection.cursor()
                cursor.execute("UPDATE seedbeds SET watering_frequency = %s WHERE id = %s;", (new_frequency, seedbed_id))
                connection.commit()
                send_specific_seedbed_data(message.chat.id, seedbed_id, message_id, greenhouse_id)
            finally:
                cursor.close()
                connection.close()
    else:
        error_text = "Ошибка: Укажите положительное целое число для частоты полива."
        markup = types.InlineKeyboardMarkup()
        back_btn = types.InlineKeyboardButton("◀️ Назад", callback_data=f"seedbed_info:{seedbed_id}:{greenhouse_id}")
        markup.add(back_btn)
        bot.edit_message_text(chat_id=message.chat.id, message_id=message_id,
                              text=error_text,
                              reply_markup=markup)

def edit_humidity(call):
    _, seedbed_id, greenhouse_id = call.data.split(':')
    markup = types.InlineKeyboardMarkup()
    back_btn = types.InlineKeyboardButton("◀️ Назад", callback_data=f"seedbed_info:{seedbed_id}:{greenhouse_id}")
    markup.add(back_btn)
    bot.edit_message_text("Введите новые пределы влажности в формате мин:макс (целые числа):",
                          chat_id=call.message.chat.id,
                          message_id=call.message.message_id,
                          reply_markup=markup)
    bot.register_next_step_handler_by_chat_id(call.message.chat.id, process_existing_humidity_input, seedbed_id, greenhouse_id, call.message.message_id)

def process_existing_humidity_input(message, seedbed_id, greenhouse_id, message_id):
    text = message.text.strip()
    bot.delete_message(message.chat.id, message.message_id)
    parts = text.split(':')
    if len(parts) == 2 and parts[0].isdigit() and parts[1].isdigit():
        min_humidity, max_humidity = map(int, parts)
        if min_humidity < max_humidity:
            connection = connect_db()
            if connection is not None:
                try:
                    cursor = connection.cursor()
                    cursor.execute("UPDATE seedbeds SET min_humidity = %s, max_humidity = %s WHERE id = %s;", (min_humidity, max_humidity, seedbed_id))
                    connection.commit()
                    send_specific_seedbed_data(message.chat.id, seedbed_id, message_id, greenhouse_id)
                finally:
                    cursor.close()
                    connection.close()
        else:
            error_text = "Ошибка: Минимальное значение влажности должно быть меньше максимального."
            markup = types.InlineKeyboardMarkup()
            back_btn = types.InlineKeyboardButton("◀️ Назад", callback_data=f"seedbed_info:{seedbed_id}:{greenhouse_id}")
            markup.add(back_btn)
            bot.edit_message_text(chat_id=message.chat.id, message_id=message_id,
                                  text=error_text,
                                  reply_markup=markup)
    else:
        error_text = "Ошибка: Пожалуйста, введите значения в формате мин:макс. Оба значения должны быть целыми числами."
        markup = types.InlineKeyboardMarkup()
        back_btn = types.InlineKeyboardButton("◀️ Назад", callback_data=f"seedbed_info:{seedbed_id}:{greenhouse_id}")
        markup.add(back_btn)
        bot.edit_message_text(chat_id=message.chat.id, message_id=message_id,
                              text=error_text,
                              reply_markup=markup)


def process_humidity_input(message, seedbed_id, greenhouse_id, message_id):
    text = message.text.strip()
    bot.delete_message(message.chat.id, message.message_id)
    parts = text.split(':')
    if len(parts) == 2 and parts[0].isdigit() and parts[1].isdigit():
        min_humidity, max_humidity = map(int, parts)
        if min_humidity < max_humidity:
            connection = connect_db()
            if connection is not None:
                try:
                    cursor = connection.cursor()
                    cursor.execute("UPDATE seedbeds SET min_humidity = %s, max_humidity = %s WHERE id = %s;", (min_humidity, max_humidity, seedbed_id))
                    connection.commit()
                    send_specific_seedbed_data(message.chat.id, seedbed_id, message_id, greenhouse_id)
                finally:
                    cursor.close()
                    connection.close()
        else:
            error_text = "Ошибка: Минимальное значение влажности должно быть меньше максимального."
            markup = types.InlineKeyboardMarkup()
            back_btn = types.InlineKeyboardButton("◀️ Назад", callback_data=f"seedbed_info:{seedbed_id}:{greenhouse_id}")
            markup.add(back_btn)
            bot.edit_message_text(chat_id=message.chat.id, message_id=message_id,
                                  text=error_text,
                                  reply_markup=markup)
    else:
        error_text = "Ошибка: Пожалуйста, введите значения в формате мин:макс. Оба значения должны быть целыми числами."
        markup = types.InlineKeyboardMarkup()
        back_btn = types.InlineKeyboardButton("◀️ Назад", callback_data=f"seedbed_info:{seedbed_id}:{greenhouse_id}")
        markup.add(back_btn)
        bot.edit_message_text(chat_id=message.chat.id, message_id=message_id,
                              text=error_text,
                              reply_markup=markup)

def toggle_active(call):
    config_id, greenhouse_id = call.data.split(':')[1:]
    connection = connect_db()
    if connection is not None:
        try:
            cursor = connection.cursor()
            cursor.execute("SELECT is_active FROM configurations WHERE id = %s;", (config_id,))
            is_active = cursor.fetchone()[0]
            new_active = not is_active
            cursor.execute("UPDATE configurations SET is_active = %s WHERE id = %s;", (new_active, config_id))
            connection.commit()
            bot.answer_callback_query(call.id, "Статус состояния изменён.")
            send_configuration_detail(call.message.chat.id, config_id, greenhouse_id, call.message.message_id)
        except Exception as e:
            bot.answer_callback_query(call.id, f"Ошибка: {str(e)}")
        finally:
            cursor.close()
            connection.close()

def toggle_auto(call):
    config_id, greenhouse_id = call.data.split(':')[1:]
    connection = connect_db()
    if connection is not None:
        try:
            cursor = connection.cursor()
            cursor.execute("SELECT is_auto FROM configurations WHERE id = %s;", (config_id,))
            is_auto = cursor.fetchone()[0]
            new_auto = not is_auto
            cursor.execute("UPDATE configurations SET is_auto = %s WHERE id = %s;", (new_auto, config_id))
            connection.commit()
            bot.answer_callback_query(call.id, "Режим работы изменён.")
            send_configuration_detail(call.message.chat.id, config_id, greenhouse_id, call.message.message_id)
        except Exception as e:
            bot.answer_callback_query(call.id, f"Ошибка: {str(e)}")
        finally:
            cursor.close()
            connection.close()

def edit_temp(call):
    config_id, greenhouse_id = call.data.split(':')[1:]
    markup = types.InlineKeyboardMarkup()
    back_btn = types.InlineKeyboardButton("◀️ Назад", callback_data=f"config_detail:{config_id}:{greenhouse_id}")
    markup.add(back_btn)
    bot.edit_message_text("Введите новые значения температуры в формате мин:макс.",
                          chat_id=call.message.chat.id,
                          message_id=call.message.message_id,
                          reply_markup=markup)
    bot.register_next_step_handler_by_chat_id(call.message.chat.id, process_temp_input, config_id, greenhouse_id, call.message.message_id)

def process_temp_input(message, config_id, greenhouse_id, message_id):
    text = message.text.strip()
    if ":" in text:
        parts = text.split(':')
        if len(parts) == 2 and parts[0].isdigit() and parts[1].isdigit():
            min_temp, max_temp = map(int, parts)
            if min_temp <= max_temp:
                connection = connect_db()
                if connection is not None:
                    cursor = connection.cursor()
                    cursor.execute("UPDATE configurations SET min_temperature = %s, max_temperature = %s WHERE id = %s;", (min_temp, max_temp, config_id))
                    connection.commit()
                    cursor.close()
                    connection.close()
                    bot.delete_message(message.chat.id, message.message_id)
                    send_configuration_detail(message.chat.id, config_id, greenhouse_id, message_id)
                    return
                else:
                    error_text = "Ошибка подключения к базе данных."
            else:
                error_text = "Минимальное значение должно быть меньше или равно максимальному."
        else:
            error_text = "Пожалуйста, введите числа."
    else:
        error_text = "Пожалуйста, введите значения в формате мин:макс."

    bot.delete_message(message.chat.id, message.message_id)
    markup = types.InlineKeyboardMarkup()
    back_btn = types.InlineKeyboardButton("◀️ Назад", callback_data=f"config_detail:{config_id}:{greenhouse_id}")
    markup.add(back_btn)
    bot.edit_message_text(chat_id=message.chat.id, message_id=message_id,
                          text=error_text,
                          reply_markup=markup)

def edit_light(call):
    config_id, greenhouse_id = call.data.split(':')[1:]
    markup = types.InlineKeyboardMarkup()
    back_btn = types.InlineKeyboardButton("◀️ Назад", callback_data=f"config_detail:{config_id}:{greenhouse_id}")
    markup.add(back_btn)
    bot.edit_message_text("Введите новые значения освещённости в формате мин:макс.",
                          chat_id=call.message.chat.id,
                          message_id=call.message.message_id,
                          reply_markup=markup)
    bot.register_next_step_handler_by_chat_id(call.message.chat.id, process_light_input, config_id, greenhouse_id, call.message.message_id)

def process_light_input(message, config_id, greenhouse_id, message_id):
    text = message.text.strip()
    if ":" in text:
        parts = text.split(':')
        if len(parts) == 2 and parts[0].isdigit() and parts[1].isdigit():
            min_light, max_light = map(int, parts)
            if min_light <= max_light:
                connection = connect_db()
                if connection is not None:
                    cursor = connection.cursor()
                    cursor.execute("UPDATE configurations SET min_light = %s, max_light = %s WHERE id = %s;", (min_light, max_light, config_id))
                    connection.commit()
                    cursor.close()
                    connection.close()
                    bot.delete_message(message.chat.id, message.message_id)
                    send_configuration_detail(message.chat.id, config_id, greenhouse_id, message_id)
                    return
                else:
                    error_text = "Ошибка подключения к базе данных."
            else:
                error_text = "Минимальное значение должно быть меньше или равно максимальному."
        else:
            error_text = "Пожалуйста, введите числа."
    else:
        error_text = "Пожалуйста, введите значения в формате мин:макс."

    bot.delete_message(message.chat.id, message.message_id)
    markup = types.InlineKeyboardMarkup()
    back_btn = types.InlineKeyboardButton("◀️ Назад", callback_data=f"config_detail:{config_id}:{greenhouse_id}")
    markup.add(back_btn)
    bot.edit_message_text(chat_id=message.chat.id, message_id=message_id,
                          text=error_text,
                          reply_markup=markup)

def ask_for_seedbed_name(message, greenhouse_id):
    identifier = create_unique_identifier()
    store_seedbed_data(identifier, (greenhouse_id, None, None, None, None, None, None, None))
    markup = types.InlineKeyboardMarkup()
    cancel_button = types.InlineKeyboardButton("Отмена", callback_data=f"cancel_seedbed_creation:{identifier}")
    markup.add(cancel_button)
    bot.edit_message_text("Введите название грядки (1-50 символов):", chat_id=message.chat.id, message_id=message.message_id, reply_markup=markup)
    bot.register_next_step_handler_by_chat_id(message.chat.id, process_seedbed_name_input, greenhouse_id, message.message_id, identifier)


def process_seedbed_name_input(message, greenhouse_id, message_id, identifier):
    seedbed_name = message.text.strip()
    bot.delete_message(message.chat.id, message.message_id)
    if 0 < len(seedbed_name) <= 50:
        data = get_seedbed_data(identifier)
        if data:
            store_seedbed_data(identifier, (greenhouse_id, seedbed_name, *data[2:]))
            ask_for_seedbed_mode(message, greenhouse_id, seedbed_name, message_id, identifier)
    else:
        error_text = "Ошибка: Название грядки должно быть от 1 до 50 символов."
        markup = types.InlineKeyboardMarkup()
        cancel_button = types.InlineKeyboardButton("Отмена", callback_data=f"cancel_seedbed_creation:{identifier}")
        markup.add(cancel_button)
        bot.edit_message_text(chat_id=message.chat.id, message_id=message_id, text=error_text, reply_markup=markup)


def ask_for_seedbed_mode(message, greenhouse_id, seedbed_name, message_id, identifier):
    store_seedbed_data(identifier, (greenhouse_id, seedbed_name, None, None, None, None, None, None))
    markup = types.InlineKeyboardMarkup()
    auto_button = types.InlineKeyboardButton("Авто", callback_data=f"seedbed_mode:auto:{identifier}")
    manual_button = types.InlineKeyboardButton("Ручной", callback_data=f"seedbed_mode:manual:{identifier}")
    cancel_button = types.InlineKeyboardButton("Отмена", callback_data=f"cancel_seedbed_creation:{identifier}")
    markup.add(auto_button, manual_button)
    markup.add(cancel_button)
    bot.edit_message_text("Выберите режим:", chat_id=message.chat.id, message_id=message_id, reply_markup=markup)


def handle_seedbed_mode(call):
    mode, identifier = call.data.split(':')[1:]
    data = get_seedbed_data(identifier)
    if data:
        greenhouse_id, seedbed_name, _, watering_enabled, watering_duration, watering_frequency, min_humidity, max_humidity = data
        is_auto = mode == "auto"
        store_seedbed_data(identifier, (greenhouse_id, seedbed_name, is_auto, watering_enabled, watering_duration, watering_frequency, min_humidity, max_humidity))
        ask_for_watering_enabled(call.message, greenhouse_id, seedbed_name, is_auto, call.message.message_id, identifier)
    else:
        bot.answer_callback_query(call.id, "Ошибка: данные не найдены.")

def ask_for_watering_enabled(message, greenhouse_id, seedbed_name, is_auto, message_id, identifier):
    markup = types.InlineKeyboardMarkup()
    enabled_button = types.InlineKeyboardButton("Включен", callback_data=f"watering_enabled:yes:{identifier}")
    disabled_button = types.InlineKeyboardButton("Выключен", callback_data=f"watering_enabled:no:{identifier}")
    cancel_button = types.InlineKeyboardButton("Отмена", callback_data=f"cancel_seedbed_creation:{identifier}")
    markup.add(enabled_button, disabled_button)
    markup.add(cancel_button)
    bot.edit_message_text("Выберите статус полива:", chat_id=message.chat.id, message_id=message_id, reply_markup=markup)



def handle_watering_enabled(call):
    enabled, identifier = call.data.split(':')[1:]
    data = get_seedbed_data(identifier)
    if data:
        greenhouse_id, seedbed_name, is_auto, _, watering_duration, watering_frequency, min_humidity, max_humidity = data
        watering_enabled = enabled == "yes"
        store_seedbed_data(identifier, (greenhouse_id, seedbed_name, is_auto, watering_enabled, watering_duration, watering_frequency, min_humidity, max_humidity))
        ask_for_watering_duration(call.message, greenhouse_id, seedbed_name, is_auto, watering_enabled, call.message.message_id, identifier)
    else:
        bot.answer_callback_query(call.id, "Ошибка: данные не найдены.")

def ask_for_watering_duration(message, greenhouse_id, seedbed_name, is_auto, watering_enabled, message_id, identifier):
    markup = types.InlineKeyboardMarkup()
    cancel_button = types.InlineKeyboardButton("Отмена", callback_data=f"cancel_seedbed_creation:{identifier}")
    markup.add(cancel_button)
    bot.edit_message_text("Введите длительность полива (положительное целое число):", chat_id=message.chat.id, message_id=message_id, reply_markup=markup)
    bot.register_next_step_handler_by_chat_id(message.chat.id, process_new_watering_duration_input, greenhouse_id, seedbed_name, is_auto, watering_enabled, message_id, identifier)


def process_new_watering_duration_input(message, greenhouse_id, seedbed_name, is_auto, watering_enabled, message_id, identifier):
    text = message.text.strip()
    bot.delete_message(message.chat.id, message.message_id)
    if text.isdigit() and int(text) > 0:
        watering_duration = int(text)
        data = get_seedbed_data(identifier)
        if data:
            store_seedbed_data(identifier, (greenhouse_id, seedbed_name, is_auto, watering_enabled, watering_duration, *data[5:]))
            ask_for_watering_frequency(message, greenhouse_id, seedbed_name, is_auto, watering_enabled, watering_duration, message_id, identifier)
    else:
        error_text = "Ошибка: Укажите положительное целое число для длительности полива."
        markup = types.InlineKeyboardMarkup()
        cancel_button = types.InlineKeyboardButton("Отмена", callback_data=f"cancel_seedbed_creation:{identifier}")
        markup.add(cancel_button)
        bot.edit_message_text(chat_id=message.chat.id, message_id=message_id, text=error_text, reply_markup=markup)




def process_watering_duration_input(message, greenhouse_id, seedbed_name, is_auto, watering_enabled, message_id, identifier):
    text = message.text.strip()
    bot.delete_message(message.chat.id, message.message_id)
    if text.isdigit() and int(text) > 0:
        watering_duration = int(text)
        data = get_seedbed_data(identifier)
        if data:
            store_seedbed_data(identifier, (greenhouse_id, seedbed_name, is_auto, watering_enabled, watering_duration, *data[5:]))
            ask_for_watering_frequency(message, greenhouse_id, seedbed_name, is_auto, watering_enabled, watering_duration, message_id, identifier)
    else:
        error_text = "Ошибка: Укажите положительное целое число для длительности полива."
        markup = types.InlineKeyboardMarkup()
        cancel_button = types.InlineKeyboardButton("Отмена", callback_data=f"cancel_seedbed_creation:{identifier}")
        markup.add(cancel_button)
        bot.edit_message_text(chat_id=message.chat.id, message_id=message_id, text=error_text, reply_markup=markup)


def ask_for_watering_frequency(message, greenhouse_id, seedbed_name, is_auto, watering_enabled, watering_duration, message_id, identifier):
    markup = types.InlineKeyboardMarkup()
    cancel_button = types.InlineKeyboardButton("Отмена", callback_data=f"cancel_seedbed_creation:{identifier}")
    markup.add(cancel_button)
    bot.edit_message_text("Введите частоту полива (положительное целое число):", chat_id=message.chat.id, message_id=message_id, reply_markup=markup)
    bot.register_next_step_handler_by_chat_id(message.chat.id, process_new_watering_frequency_input, greenhouse_id, seedbed_name, is_auto, watering_enabled, watering_duration, message_id, identifier)

def process_new_watering_frequency_input(message, greenhouse_id, seedbed_name, is_auto, watering_enabled, watering_duration, message_id, identifier):
    text = message.text.strip()
    bot.delete_message(message.chat.id, message.message_id)
    if text.isdigit() and int(text) > 0:
        watering_frequency = int(text)
        data = get_seedbed_data(identifier)
        if data:
            store_seedbed_data(identifier, (greenhouse_id, seedbed_name, is_auto, watering_enabled, watering_duration, watering_frequency, *data[6:]))
            ask_for_humidity_range(message, greenhouse_id, seedbed_name, is_auto, watering_enabled, watering_duration, watering_frequency, message_id, identifier)
    else:
        error_text = "Ошибка: Укажите положительное целое число для частоты полива."
        markup = types.InlineKeyboardMarkup()
        cancel_button = types.InlineKeyboardButton("Отмена", callback_data=f"cancel_seedbed_creation:{identifier}")
        markup.add(cancel_button)
        bot.edit_message_text(chat_id=message.chat.id, message_id=message_id, text=error_text, reply_markup=markup)


def process_watering_frequency_input(message, greenhouse_id, seedbed_name, is_auto, watering_enabled, watering_duration, message_id, identifier):
    text = message.text.strip()
    bot.delete_message(message.chat.id, message.message_id)
    if text.isdigit() and int(text) > 0:
        watering_frequency = int(text)
        data = get_seedbed_data(identifier)
        if data:
            store_seedbed_data(identifier, (greenhouse_id, seedbed_name, is_auto, watering_enabled, watering_duration, watering_frequency, *data[6:]))
            ask_for_humidity_range(message, greenhouse_id, seedbed_name, is_auto, watering_enabled, watering_duration, watering_frequency, message_id, identifier)
    else:
        error_text = "Ошибка: Укажите положительное целое число для частоты полива."
        markup = types.InlineKeyboardMarkup()
        cancel_button = types.InlineKeyboardButton("Отмена", callback_data=f"cancel_seedbed_creation:{identifier}")
        markup.add(cancel_button)
        bot.edit_message_text(chat_id=message.chat.id, message_id=message_id, text=error_text, reply_markup=markup)


def ask_for_humidity_range(message, greenhouse_id, seedbed_name, is_auto, watering_enabled, watering_duration, watering_frequency, message_id, identifier):
    markup = types.InlineKeyboardMarkup()
    cancel_button = types.InlineKeyboardButton("Отмена", callback_data=f"cancel_seedbed_creation:{identifier}")
    markup.add(cancel_button)
    bot.edit_message_text("Введите пределы влажности в формате мин:макс (целые числа):", chat_id=message.chat.id, message_id=message_id, reply_markup=markup)
    bot.register_next_step_handler_by_chat_id(message.chat.id, process_new_humidity_input, greenhouse_id, seedbed_name, is_auto, watering_enabled, watering_duration, watering_frequency, message_id, identifier)


def process_new_humidity_input(message, greenhouse_id, seedbed_name, is_auto, watering_enabled, watering_duration, watering_frequency, message_id, identifier):
    text = message.text.strip()
    bot.delete_message(message.chat.id, message.message_id)
    parts = text.split(':')
    if len(parts) == 2 and parts[0].isdigit() and parts[1].isdigit():
        min_humidity, max_humidity = map(int, parts)
        if min_humidity < max_humidity:
            data = get_seedbed_data(identifier)
            if data:
                store_seedbed_data(identifier, (greenhouse_id, seedbed_name, is_auto, watering_enabled, watering_duration, watering_frequency, min_humidity, max_humidity))
                confirm_seedbed_creation(message, greenhouse_id, seedbed_name, is_auto, watering_enabled, watering_duration, watering_frequency, min_humidity, max_humidity, message_id, identifier)
        else:
            error_text = "Ошибка: Минимальное значение влажности должно быть меньше максимального."
            markup = types.InlineKeyboardMarkup()
            cancel_button = types.InlineKeyboardButton("Отмена", callback_data=f"cancel_seedbed_creation:{identifier}")
            markup.add(cancel_button)
            bot.edit_message_text(chat_id=message.chat.id, message_id=message_id, text=error_text, reply_markup=markup)
    else:
        error_text = "Ошибка: Пожалуйста, введите значения в формате мин:макс. Оба значения должны быть целыми числами."
        markup = types.InlineKeyboardMarkup()
        cancel_button = types.InlineKeyboardButton("Отмена", callback_data=f"cancel_seedbed_creation:{identifier}")
        markup.add(cancel_button)
        bot.edit_message_text(chat_id=message.chat.id, message_id=message_id, text=error_text, reply_markup=markup)


def process_humidity_input(message, greenhouse_id, seedbed_name, is_auto, watering_enabled, watering_duration, watering_frequency, message_id, identifier):
    text = message.text.strip()
    bot.delete_message(message.chat.id, message.message_id)
    parts = text.split(':')
    if len(parts) == 2 and parts[0].isdigit() and parts[1].isdigit():
        min_humidity, max_humidity = map(int, parts)
        if min_humidity < max_humidity:
            data = get_seedbed_data(identifier)
            if data:
                store_seedbed_data(identifier, (greenhouse_id, seedbed_name, is_auto, watering_enabled, watering_duration, watering_frequency, min_humidity, max_humidity))
                confirm_seedbed_creation(message, greenhouse_id, seedbed_name, is_auto, watering_enabled, watering_duration, watering_frequency, min_humidity, max_humidity, message_id, identifier)
        else:
            error_text = "Ошибка: Минимальное значение влажности должно быть меньше максимального."
            markup = types.InlineKeyboardMarkup()
            cancel_button = types.InlineKeyboardButton("Отмена", callback_data=f"cancel_seedbed_creation:{identifier}")
            markup.add(cancel_button)
            bot.edit_message_text(chat_id=message.chat.id, message_id=message_id, text=error_text, reply_markup=markup)
    else:
        error_text = "Ошибка: Пожалуйста, введите значения в формате мин:макс. Оба значения должны быть целыми числами."
        markup = types.InlineKeyboardMarkup()
        cancel_button = types.InlineKeyboardButton("Отмена", callback_data=f"cancel_seedbed_creation:{identifier}")
        markup.add(cancel_button)
        bot.edit_message_text(chat_id=message.chat.id, message_id=message_id, text=error_text, reply_markup=markup)


def confirm_seedbed_creation(message, greenhouse_id, seedbed_name, is_auto, watering_enabled, watering_duration, watering_frequency, min_humidity, max_humidity, message_id, identifier):
    markup = types.InlineKeyboardMarkup()
    save_button = types.InlineKeyboardButton("Сохранить", callback_data=f"save_seedbed:{identifier}")
    cancel_button = types.InlineKeyboardButton("Отмена", callback_data=f"cancel_seedbed_creation:{identifier}")
    markup.add(save_button, cancel_button)
    summary = (f"Название грядки: {seedbed_name}\nРежим: {'Авто' if is_auto else 'Ручной'}\nПолив: {'Включен' if watering_enabled else 'Выключен'}\n"
               f"Длительность полива: {watering_duration}\nЧастота полива: {watering_frequency}\nМин. влажность: {min_humidity}\nМакс. влажность: {max_humidity}")
    bot.edit_message_text(chat_id=message.chat.id, message_id=message_id, text=summary, reply_markup=markup)

def handle_save_seedbed(call):
    identifier = call.data.split(':')[1]
    data = get_seedbed_data(identifier)
    if data:
        greenhouse_id, seedbed_name, is_auto, watering_enabled, watering_duration, watering_frequency, min_humidity, max_humidity = data
        connection = connect_db()
        if connection is not None:
            try:
                cursor = connection.cursor()
                cursor.execute(
                    "INSERT INTO seedbeds (greenhouse_id, seedbed_name, is_auto, watering_enabled, watering_duration, watering_frequency, min_humidity, max_humidity) VALUES (%s, %s, %s, %s, %s, %s, %s, %s);",
                    (greenhouse_id, seedbed_name, is_auto, watering_enabled, watering_duration, watering_frequency, min_humidity, max_humidity))
                connection.commit()
                bot.answer_callback_query(call.id, "Грядка успешно добавлена.")
                send_specific_greenhouse_data(call.message.chat.id, greenhouse_id, call.message.message_id)
                remove_seedbed_data(identifier)
            except Exception as e:
                bot.answer_callback_query(call.id, f"Ошибка: {str(e)}")
            finally:
                cursor.close()
                connection.close()
    else:
        bot.answer_callback_query(call.id, "Ошибка: данные не найдены.")


@bot.message_handler(func=lambda message: message.text == "🛰️ Информация о сервере")
def server_info(message, message_id=None):
    headers = {'Authorization': f'Bearer {jwt_token}'}
    response = requests.get("https://smartghouse.ru/api/status", headers=headers)
    if response.status_code == 200:
        data = response.json()
        
        clients_list = [f"{i + 1}. {', '.join(services)} ({ip})"
                        for i, (ip, services) in enumerate(data['clients'].items())]
        clients_info = "\n".join(clients_list)
        tunnels_info = data['tunnels']
        mem_info = f"RSS: {data['mem']['rss']/1024**2:.2f} MB, \n" \
                   f"HeapTotal: {data['mem']['heapTotal']/1024**2:.2f} MB, \n" \
                   f"HeapUsed: {data['mem']['heapUsed']/1024**2:.2f} MB, \n" \
                   f"External: {data['mem']['external']/1024**2:.2f} MB"

        markup = types.InlineKeyboardMarkup()
        for tunnel_name in data['clients'].values():
            for name in tunnel_name:
                callback_data = f"close_tunnel:{name}"
                markup.add(types.InlineKeyboardButton(f"Закрыть {name}", callback_data=callback_data))
  
        btn_back = types.InlineKeyboardButton("◀️ Назад", callback_data='tunnel_management')
        markup.add(btn_back)

        info_message = f"Клиенты:\n{clients_info}\n\n" \
                       f"Туннели: {tunnels_info}\n\n" \
                       f"Память:\n{mem_info}"
        
        if message_id:
            bot.edit_message_text(chat_id=message.chat.id, message_id=message_id, text=info_message, reply_markup=markup)
        else:
            bot.send_message(message.chat.id, info_message, reply_markup=markup)
    else:
        error_msg = "Произошла ошибка при получении данных с сервера."
        if message_id:
            bot.edit_message_text(chat_id=message.chat.id, message_id=message_id, text=error_msg)
        else:
            bot.reply_to(message, error_msg)


@bot.callback_query_handler(func=lambda call: True)
def callback_query(call):
    chat_id = call.message.chat.id
    user_id = call.from_user.id
    message_id = call.message.message_id

    if user_id not in authorized_users:
        bot.edit_message_text(chat_id=chat_id, message_id=message_id, text=f"Требуется повторная авторизация\nВведите команду /start", reply_markup=None)
        bot.answer_callback_query(call.id)
    else:
        if call.data == 'greenhouse_info':
            send_greenhouse_menu(chat_id, message_id)
        elif call.data == 'server_info':
            server_info(call.message, message_id)
        elif call.data == 'raise_tunnel':
            ask_for_subdomain(call.message, message_id)
        elif call.data == 'main_menu':
            send_main_menu(chat_id, message_id)
        elif call.data == 'tunnel_management':
            send_tunnel_management_menu(chat_id, message_id)
        elif call.data == 'restart_device':
            confirm_restart(call)
        elif call.data == 'confirm_restart':
            execute_restart(call)
        elif call.data.startswith("configurations:"):
            handle_configs_selection(call)
        elif call.data.startswith("config_detail:"):
            handle_config_detail_selection(call)
        elif call.data.startswith("greenhouse_data:"):
            handle_greenhouse_selection(call)
        elif call.data.startswith("seedbed_info:"):
            handle_seedbed_selection(call)
        elif call.data.startswith("add_seedbed:"):
            handle_add_seedbed(call)
        elif call.data.startswith("seedbed_mode:"):
            handle_seedbed_mode(call)
        elif call.data.startswith("watering_enabled:"):
            handle_watering_enabled(call)
        elif call.data.startswith("save_seedbed:"):
            handle_save_seedbed(call)
        elif call.data.startswith("toggle_mode:"):
            toggle_mode(call)
        elif call.data.startswith("toggle_watering:"):
            toggle_watering(call)
        elif call.data.startswith("edit_name:"):
            edit_name(call)
        elif call.data.startswith("edit_watering_duration:"):
            edit_watering_duration(call)
        elif call.data.startswith("edit_watering_frequency:"):
            edit_watering_frequency(call)
        elif call.data.startswith("edit_humidity:"):
            edit_humidity(call)
        elif call.data.startswith("delete_seedbed:"):
            confirm_delete_seedbed(call)
        elif call.data.startswith("confirm_delete_seedbed:"):
            handle_delete_seedbed(call)
        elif call.data.startswith("toggle_active:"):
            toggle_active(call)
        elif call.data.startswith("toggle_auto:"):
            toggle_auto(call)
        elif call.data.startswith("edit_temp:"):
            edit_temp(call)
        elif call.data.startswith("edit_light:"):
            edit_light(call)
        elif call.data.startswith("close_tunnel:"):
            handle_close_tunnel(call)
        elif call.data.startswith("cancel_seedbed_creation:"):
            handle_cancel_seedbed_creation(call)
        bot.answer_callback_query(call.id)


@bot.message_handler(func=lambda message: message.text == "🏡 Управление теплицами")
def greenhouse_info(message):
    send_greenhouse_menu(message.chat.id)

@bot.message_handler(func=lambda message: message.text == "Меню")
def handle_back_to_main_menu(message):
    send_main_menu(message.chat.id)

def handle_cancel_seedbed_creation(call):
    identifier = call.data.split(':')[1]
    data = get_seedbed_data(identifier)
    greenhouse_id = data[0] if data else None
    remove_seedbed_data(identifier)
    if greenhouse_id:
        send_specific_greenhouse_data(call.message.chat.id, greenhouse_id, call.message.message_id)
    else:
        send_main_menu(call.message.chat.id, call.message.message_id)
    print(seedbed_data)
    bot.answer_callback_query(call.id, "Создание грядки отменено.")


def handle_add_seedbed(call):
    greenhouse_id = call.data.split(':')[1]
    ask_for_seedbed_name(call.message, greenhouse_id)


def handle_greenhouse_selection(call):
    chat_id = call.message.chat.id
    message_id = call.message.message_id
    greenhouse_id = call.data.split(':')[1]
    send_specific_greenhouse_data(chat_id, greenhouse_id, message_id)

def handle_seedbed_selection(call):
    _, seedbed_id, greenhouse_id = call.data.split(':')
    chat_id = call.message.chat.id
    message_id = call.message.message_id
    send_specific_seedbed_data(chat_id, seedbed_id, message_id, greenhouse_id)

def handle_configs_selection(call):
    chat_id = call.message.chat.id
    message_id = call.message.message_id
    greenhouse_id = call.data.split(':')[1]
    send_configurations_menu(chat_id, greenhouse_id, message_id)

def handle_config_detail_selection(call):
    chat_id = call.message.chat.id
    message_id = call.message.message_id
    config_id, greenhouse_id = call.data.split(':')[1:]
    send_configuration_detail(chat_id, config_id, greenhouse_id, message_id)

def confirm_delete_seedbed(call):
    seedbed_id, greenhouse_id = call.data.split(':')[1:]
    markup = types.InlineKeyboardMarkup()
    confirm_btn = types.InlineKeyboardButton("Подтвердить", callback_data=f"confirm_delete_seedbed:{seedbed_id}:{greenhouse_id}")
    cancel_btn = types.InlineKeyboardButton("Отмена", callback_data=f"seedbed_info:{seedbed_id}:{greenhouse_id}")
    markup.add(confirm_btn, cancel_btn)
    warning_message = f"⚠️ Внимание!\nВы уверены, что хотите удалить эту грядку? Это действие необратимо."
    bot.edit_message_text(chat_id=call.message.chat.id, message_id=call.message.message_id, text=warning_message, reply_markup=markup)

def handle_delete_seedbed(call):
    seedbed_id, greenhouse_id = call.data.split(':')[1:]
    connection = connect_db()
    if connection is not None:
        try:
            cursor = connection.cursor()
            cursor.execute("DELETE FROM seedbeds WHERE id = %s;", (seedbed_id,))
            connection.commit()
            bot.answer_callback_query(call.id, "Грядка успешно удалена.")
            send_specific_greenhouse_data(call.message.chat.id, greenhouse_id, call.message.message_id)
        except Exception as e:
            bot.answer_callback_query(call.id, f"Ошибка: {str(e)}")
        finally:
            cursor.close()
            connection.close()


def handle_close_tunnel(call):
    global active_tunnel_subdomain
    headers = {'Authorization': f'Bearer {jwt_token}'}
    tunnel_id = call.data.split(':')[1]
    response = requests.post(f"https://smartghouse.ru/api/tunnels/{tunnel_id}/close", headers=headers)
    active_tunnel_subdomain = None
    if response.status_code == 200:
        markup = types.InlineKeyboardMarkup()
        back_button = types.InlineKeyboardButton("◀️ Назад", callback_data='server_info')
        markup.add(back_button)
        bot.edit_message_text(chat_id=call.message.chat.id, message_id=call.message.message_id, text=f"Туннель {tunnel_id} успешно закрыт\nОбновление информации займёт 45 секунд", reply_markup=markup)
    else:
        bot.edit_message_text(chat_id=call.message.chat.id, message_id=call.message.message_id, text=f"Произошла ошибка при закрытии туннеля", reply_markup=markup)


def remove_ansi_escape_sequences(text):
    ansi_escape_pattern = re.compile(r'\x1B[@-_][0-?]*[ -/]*[@-~]')
    return ansi_escape_pattern.sub('', text)

@bot.message_handler(func=lambda message: message.text == "🚀 Активация туннеля")
def ask_for_subdomain(message, message_id):
    markup = types.InlineKeyboardMarkup()
    cancel_button = types.InlineKeyboardButton("Отмена", callback_data='tunnel_management')
    markup.add(cancel_button)
    bot.edit_message_text("Введите название поддомена для туннеля:", chat_id=message.chat.id, message_id=message_id, reply_markup=markup)
    bot.register_next_step_handler_by_chat_id(message.chat.id, process_subdomain_input, message_id)

def process_subdomain_input(message, message_id):
    if message.text is None:
        bot.delete_message(message.chat.id, message.message_id)
        markup = types.InlineKeyboardMarkup()
        menu_button = types.InlineKeyboardButton("Меню", callback_data='main_menu')
        markup.add(menu_button)
        error_message = "Название поддомена некорректное. Пожалуйста, используйте только буквы нижнего регистра и цифры, длина от 4 до 63 символов."
        bot.edit_message_text(chat_id=message.chat.id, message_id=message_id, text=error_message, reply_markup=markup)
    else:
        subdomain = message.text.strip()
        if re.match(r'^[a-z0-9]{4,63}$', subdomain):
            bot.delete_message(message.chat.id, message.message_id)
            tunnel_setup(message, subdomain, message_id)
        else:
            bot.delete_message(message.chat.id, message.message_id)
            markup = types.InlineKeyboardMarkup()
            menu_button = types.InlineKeyboardButton("Меню", callback_data='main_menu')
            markup.add(menu_button)
            error_message = "Название поддомена некорректное. Пожалуйста, используйте только буквы нижнего регистра и цифры, длина от 4 до 63 символов."
            bot.edit_message_text(chat_id=message.chat.id, message_id=message_id, text=error_message, reply_markup=markup)


def tunnel_setup(message, subdomain, message_id):
    global active_tunnel_subdomain
    active_tunnel_subdomain = subdomain
    def run_tunnel():
        command = ["/home/router/smartghouse_client/bin/lt.js", "--host", "http://smartghouse.ru", "--port", "80", "--subdomain", subdomain, "--jwt", args.token_file]
        process = subprocess.Popen(command, start_new_session=True)

    thread = threading.Thread(target=run_tunnel)
    thread.start()

    markup = types.InlineKeyboardMarkup()
    menu_button = types.InlineKeyboardButton("Меню", callback_data='main_menu')
    markup.add(menu_button)
    msg = f"Тунель активирован!\nВаш URL: https://{subdomain}.smartghouse.ru"
    bot.edit_message_text(chat_id=message.chat.id, message_id=message_id, text=msg, reply_markup=markup)


def is_any_tunnel_active():
    try:
        result = subprocess.run(
            ["pgrep", "-f", "lt.js"],
            text=True,
            capture_output=True
        )
        return result.returncode == 0
    except Exception as e:
        print(f"Error checking tunnel status: {e}")
        return False

@bot.callback_query_handler(func=lambda call: call.data == 'restart_device')
def confirm_restart(call):
    markup = types.InlineKeyboardMarkup()
    btn_ok = types.InlineKeyboardButton("ОК", callback_data='confirm_restart')
    btn_back = types.InlineKeyboardButton("◀️ Назад", callback_data='main_menu')
    markup.add(btn_ok)
    markup.add(btn_back)
    warning_message = f"⚠️ Внимание!\nПерезагрузка устройства приведет к закрытию активных туннелей и потребует повторной авторизации."
    bot.edit_message_text(chat_id=call.message.chat.id, message_id=call.message.message_id, text=warning_message, reply_markup=markup)

@bot.callback_query_handler(func=lambda call: call.data == 'confirm_restart')
def execute_restart(call):
    bot.edit_message_text(chat_id=call.message.chat.id, message_id=call.message.message_id, text=f"Выполняется перезагрузка...\nДля повторной авторизации введите команду /start через 1 минуту.", reply_markup=None)
    subprocess.call(['sudo', 'reboot'])

@bot.message_handler(func=lambda message: message.text == "◀️ Назад")
def handle_back(message):
    send_greenhouse_menu(message.chat.id)

@bot.message_handler(func=lambda message: message.text == "Видеонаблюдение")
def handle_surveillance(message):
    # Заглушка: добавьте логику для удаленного видеонаблюдения
    bot.reply_to(message, "Функция видеонаблюдения в разработке")

def check_tunnel_availability():
    global active_tunnel_subdomain
    if active_tunnel_subdomain:
        url = f"https://{active_tunnel_subdomain}.smartghouse.ru"
        try:
            response = requests.get(url, timeout=10, allow_redirects=True)
            final_url = response.url
            if "theboroer.github.io/localtunnel-www" in final_url:
                raise Exception("Tunnel is down due to redirect to Localtunnel failure page")
        except Exception as e:
            for user_id in authorized_users:
                bot.send_message(user_id, f"Внимание: туннель {active_tunnel_subdomain} был остановлен.")
            active_tunnel_subdomain = None
        finally:
            Timer(300, check_tunnel_availability).start()

Timer(300, check_tunnel_availability).start()


if __name__ == '__main__':
    bot.polling(none_stop=True)