select p.name, c.name
from person as p join company as c
on c.id = p.company_id
where p.company_id != 5;