insert into cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance) values (9, 'Spa', 20, 30, 100000, 800)

insert into cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance) values ((select max(facid)from cd.facilities)+1, 'Spa', 20, 30, 100000, 800)

update cd.facilities set initialoutlay=10000 where name='Tennis Court 2'

update cd.facilities set membercost=(select membercost from cd.facilities where name like 'Tennis Court 1')*1.1, guestcost=(select guestcost from cd.facilities where name like 'Tennis Court 1')*1.1 where name='Tennis Court 2'

delete from cd.bookings

delete from cd.members where memid=37

select facid, name, membercost, monthlymaintenance from cd.facilities where membercost<monthlymaintenance/50 and membercost>0

select * from cd.facilities where name like '%Tennis%'

select * from cd.facilities where facid=1 or facid=5

select memid, surname, firstname, joindate from cd.members where joindate>'2012-09-01 00:00:00'

select name from cd.facilities union select surname from cd.members

select starttime from cd.bookings join cd.members on cd.bookings.memid=cd.members.memid where firstname='David' and surname='Farrell'

select cd.bookings.starttime as start, cd.facilities.name from cd.bookings join cd.facilities on cd.bookings.facid=cd.facilities.facid where cd.facilities.name like 'Tennis%' and cd.bookings.starttime<'2012-09-22 00:00:00' and cd.bookings.starttime>'2012-09-21 00:00:00' order by cd.bookings.starttime

select cd.members.firstname as memfname, cd.members.surname as memsname, rec.firstname as recfname, rec.surname as recsname from cd.members left outer join cd.members rec on cd.members.recommendedby=rec.memid order by memsname, memfname

select distinct cd.members.firstname, cd.members.surname from cd.members inner join cd.members rec on cd.members.memid=rec.recommendedby order by surname, firstname

select distinct mems.firstname || ' ' || mems.surname as member, (select recs.firstname || ' ' || recs.surname as recommender from cd.members recs where recs.memid=mems.recommendedby) from cd.members mems order by member

select recommendedby, count(*) from cd.members where recommendedby is not null group by recommendedby order by recommendedby

 select facid, sum(slots) from cd.bookings group by facid order by facid

select book.facid, sum(book.slots) as "Total Slots" from cd.bookings book where book.starttime>'2012-09-01 00:00:00' and book.starttime<'2012-10-01 00:00:00' group by book.facid order by "Total Slots"

select facid, extract(month from starttime) as month, sum(slots) as "Total Slots" from cd.bookings where extract(year from starttime)=2012 group by facid, month order by facid, month

select count(distinct mems.*) from cd.members mems join cd.bookings book on mems.memid=book.memid where book.facid is not null 

select mems.surname, mems.firstname, mems.memid, min(book.starttime) from cd.members mems join cd.bookings book on mems.memid=book.memid where book.starttime>'2012-09-01 00:00:00' group by mems.memid, mems.surname, mems.firstname order by memid

select count(*) over(), firstname, surname from cd.members

select row_number() over(order by joindate), firstname, surname from cd.members

select facid, total from (select facid, sum(slots) total, rank() over(order by sum(slots) desc) from cd.bookings group by facid) where rank=1

select surname || ', ' || firstname as name from cd.members

select memid, telephone from cd.members where telephone like '%(%' or telephone like '%)%'

select substr(surname,1,1) as letter, count(*) from cd.members group by letter order by letter


