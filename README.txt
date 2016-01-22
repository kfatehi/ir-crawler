If you wish to convey anything to the TA, please put it here.

Creating a Postgres Database (Production)

DROP DATABASE IF EXISTS ir_prod;
CREATE USER ir_prod WITH PASSWORD '...';
CREATE DATABASE ir_prod;
GRANT ALL PRIVILEGES ON DATABASE ir_prod to ir_prod;

Creating a Postgres Database (Test)

DROP DATABASE IF EXISTS ir_test;
CREATE USER ir_test WITH PASSWORD '...';
CREATE DATABASE ir_test;
GRANT ALL PRIVILEGES ON DATABASE ir_test to ir_test;

Setting database passwords

echo "thepassword" > _private/prod_db_password.txt

echo "thepassword" > _private/test_db_password.txt

Running with Log output directed to public web

make crawl >> ../public_html/crawler.txt 2>&1

Log readable at this URL

http://keyvan.pw:22080/~keyvan/crawler.txt
