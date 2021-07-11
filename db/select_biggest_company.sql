select counted.name, counted.count from
(select c.name, count(p.company_id)
 from company as c
 join person as p
 on c.id = p.company_id
 group by c.name) as counted
 where counted.count = (select max(count) FROM (select c.name, count(p.company_id)
 from company as c
 join person as p
 on c.id = p.company_id
 group by c.name) as counted);