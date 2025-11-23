# Introduction
This is a prject hat operates different things on sql databses such as joins, over, group by and mahy more.

# create the tables 
```sql
CREATE TABLE cd.members
    (
       memid integer NOT NULL, 
       surname character varying(200) NOT NULL, 
       firstname character varying(200) NOT NULL, 
       address character varying(300) NOT NULL, 
       zipcode integer NOT NULL, 
       telephone character varying(20) NOT NULL, 
       recommendedby integer,
       joindate timestamp NOT NULL,
       CONSTRAINT members_pk PRIMARY KEY (memid),
       CONSTRAINT fk_members_recommendedby FOREIGN KEY (recommendedby)
            REFERENCES cd.members(memid) ON DELETE SET NULL
    );
```
```sql
CREATE TABLE cd.facilities
    (
       facid integer NOT NULL, 
       name character varying(100) NOT NULL, 
       membercost numeric NOT NULL, 
       guestcost numeric NOT NULL, 
       initialoutlay numeric NOT NULL, 
       monthlymaintenance numeric NOT NULL, 
       CONSTRAINT facilities_pk PRIMARY KEY (facid)
    );
```
```sql
CREATE TABLE cd.bookings
    (
       bookid integer NOT NULL, 
       facid integer NOT NULL, 
       memid integer NOT NULL, 
       starttime timestamp NOT NULL,
       slots integer NOT NULL,
       CONSTRAINT bookings_pk PRIMARY KEY (bookid),
       CONSTRAINT fk_bookings_facid FOREIGN KEY (facid) REFERENCES cd.facilities(facid),
       CONSTRAINT fk_bookings_memid FOREIGN KEY (memid) REFERENCES cd.members(memid)
    );
```




# Insert some data into a table
```sql
insert into cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance) values (9, 'Spa', 20, 30, 100000, 800)
```
# Insert calculated data into a table
```sql
insert into cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance) values ((select max(facid)from cd.facilities)+1, 'Spa', 20, 30, 100000, 800)
```
# Update some existing data
```sql
update cd.facilities set initialoutlay=10000 where name='Tennis Court 2'
```
# Update a row based on the contents of another row
```sql
update cd.facilities set membercost=(select membercost from cd.facilities where name like 'Tennis Court 1')*1.1, guestcost=(select guestcost from cd.facilities where name like 'Tennis Court 1')*1.1 where name='Tennis Court 2'
```
# Delete all bookings
```sql
delete from cd.bookings
```
# Delete a member from the cd.members table
```sql
delete from cd.members where memid=37
```
# Control which rows are retrieved - part 2
```sql
select facid, name, membercost, monthlymaintenance from cd.facilities where membercost<monthlymaintenance/50 and membercost>0
```
# Basic string searches
```sql
select * from cd.facilities where name like '%Tennis%'
```
# Matching against multiple possible values
```sql
select * from cd.facilities where facid=1 or facid=5
```
# Working with dates
```sql
select memid, surname, firstname, joindate from cd.members where joindate>'2012-09-01 00:00:00'
```
# Combining results from multiple queries
```sql
select name from cd.facilities union select surname from cd.members
```
# Retrieve the start times of members' bookings
```sql
select starttime from cd.bookings join cd.members on cd.bookings.memid=cd.members.memid where firstname='David' and surname='Farrell'
```
# Work out the start times of bookings for tennis courts
```sql
select cd.bookings.starttime as start, cd.facilities.name from cd.bookings join cd.facilities on cd.bookings.facid=cd.facilities.facid where cd.facilities.name like 'Tennis%' and cd.bookings.starttime<'2012-09-22 00:00:00' and cd.bookings.starttime>'2012-09-21 00:00:00' order by cd.bookings.starttime
```
#  Produce a list of all members, along with their recommender 
```sql
select cd.members.firstname as memfname, cd.members.surname as memsname, rec.firstname as recfname, rec.surname as recsname from cd.members left outer join cd.members rec on cd.members.recommendedby=rec.memid order by memsname, memfname
```
#  Produce a list of all members who have recommended another member 
```sql
select distinct cd.members.firstname, cd.members.surname from cd.members inner join cd.members rec on cd.members.memid=rec.recommendedby order by surname, firstname
```
#  Produce a list of all members, along with their recommender, using no joins. 
```sql
select distinct mems.firstname || ' ' || mems.surname as member, (select recs.firstname || ' ' || recs.surname as recommender from cd.members recs where recs.memid=mems.recommendedby) from cd.members mems order by member
```
#  Count the number of recommendations each member makes. 
```sql
select recommendedby, count(*) from cd.members where recommendedby is not null group by recommendedby order by recommendedby
```
#  List the total slots booked per facility 
```sql
 select facid, sum(slots) from cd.bookings group by facid order by facid
```
#  List the total slots booked per facility in a given month 
```sql
select book.facid, sum(book.slots) as "Total Slots" from cd.bookings book where book.starttime>'2012-09-01 00:00:00' and book.starttime<'2012-10-01 00:00:00' group by book.facid order by "Total Slots"
```
#  List the total slots booked per facility per month 
```sql
select facid, extract(month from starttime) as month, sum(slots) as "Total Slots" from cd.bookings where extract(year from starttime)=2012 group by facid, month order by facid, month
```
#  Find the count of members who have made at least one booking 
```sql
select count(distinct mems.*) from cd.members mems join cd.bookings book on mems.memid=book.memid where book.facid is not null 
```
#  List each member's first booking after September 1st 2012 
```sql
select mems.surname, mems.firstname, mems.memid, min(book.starttime) from cd.members mems join cd.bookings book on mems.memid=book.memid where book.starttime>'2012-09-01 00:00:00' group by mems.memid, mems.surname, mems.firstname order by memid
```
#  Produce a list of member names, with each row containing the total member count 
```sql
select count(*) over(), firstname, surname from cd.members
```
#  Produce a numbered list of members 
```sql
select row_number() over(order by joindate), firstname, surname from cd.members
```
#  Output the facility id that has the highest number of slots booked, again 
```sql
select facid, total from (select facid, sum(slots) total, rank() over(order by sum(slots) desc) from cd.bookings group by facid) where rank=1
```
#  Format the names of members 
```sql
select surname || ', ' || firstname as name from cd.members
```
#  Find telephone numbers with parentheses 
```sql
select memid, telephone from cd.members where telephone like '%(%' or telephone like '%)%'
```
#  Count the number of members whose surname starts with each letter of the alphabet 
```sql
select substr(surname,1,1) as letter, count(*) from cd.members group by letter order by letter
```

