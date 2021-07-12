create view counted as
select c.name, count(p.company_id)
 from company as c
 join person as p
 on c.id = p.company_id
 group by c.name;

select counted.name, counted.count from counted
 where counted.count = (select max(count) from counted);