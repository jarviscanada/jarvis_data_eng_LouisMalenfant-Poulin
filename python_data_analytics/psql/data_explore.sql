-- Show table schema
\d+ retail

-- Show first 10 rows
select * from retail limit 10;

--Check # of records
select count(*) from retail;

--number of clients (Unique)
select count(distinct customer_id) from retail;

--invoice date range (e.g. max/min dates)
select max(invoice_date) as max, min(invoice_date) as min from retail;

--number of SKU/merchants (e.g. unique stock code)
select count(distinct stock_code) from retail;

--Calculate average invoice amount excluding invoices with a negative amount (e.g. canceled orders have negative amount)
select avg(somme) from (select sum(unit_price*quantity) as somme from retail group by invoice_no) as tabb where somme>0;

--Calculate total revenue (e.g. sum of unit_price * quantity)
select sum(unit_price*quantity) from retail;

--Calculate total revenue by YYYYMM 
select time, sum(unit_price*quantity) from (select unit_price, quantity, to_char(invoice_date, 'YYYYMM') as time from retail) as tabb group by time order by time;
