FROM mcr.microsoft.com/mssql/server:2022-latest

USER root

RUN apt-get update
RUN apt-get install -y gettext-base

ADD entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

ADD create_db.sql.template create_db.sql.template

ENTRYPOINT ["bash", "-c", "/entrypoint.sh"]
