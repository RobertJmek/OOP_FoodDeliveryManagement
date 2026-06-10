FROM mysql:8.0

ENV MYSQL_ROOT_PASSWORD=root_pass
ENV MYSQL_DATABASE=delivery_management_db
ENV MYSQL_USER=delivery_user
ENV MYSQL_PASSWORD=delivery_pass

EXPOSE 3306

