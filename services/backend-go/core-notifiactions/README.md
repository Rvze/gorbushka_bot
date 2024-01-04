## CORE NOTIFICATIONS
Сервис предназначен для рассылки пользователям сообщений об изменениях в моделях за которыми они следят.

### Переменные окружения
    - HTTP_HOST       
    - HTTP_PORT       
    - REDIS_HOST      
    - REDIS_PORT      
    - REDIS_TOPIC     
    - DB_HOST         
    - DB_PORT         
    - DB_USER        
    - DB_PASSWORD     
    - TG_TOKEN        
### Скрипт создания таблицы

    CREATE TABLE subscription_notify (
        tg_chat_id bigint NOT NULL,
        model_id varchar(255) NOT NULL,
        model_type bigint NOT NULL,
        PRIMARY KEY (tg_chat_id, model_type)
    )
