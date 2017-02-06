-- Update offset
UPDATE action.actionrule
SET surveydatedaysoffset  = surveydatedaysoffset +2
WHERE surveydatedaysoffset in(-45,-38,-5);

UPDATE action.actionrule
SET surveydatedaysoffset  = surveydatedaysoffset +1
WHERE surveydatedaysoffset in(1,8,16);

UPDATE action.actionrule
SET surveydatedaysoffset  = surveydatedaysoffset +4
WHERE surveydatedaysoffset in(4);



-- Update action rules name and description
UPDATE action.actionrule
SET name = replace(name,'-45','-43')
,   description = replace(description,'-45','-43')
WHERE surveydatedaysoffset = -43;

UPDATE action.actionrule
SET name = replace(name,'-5','-3')
,   description = replace(description,'-5','-3')
WHERE surveydatedaysoffset = -3;

UPDATE action.actionrule
SET name = replace(name,'-38','-36')
,   description = replace(description,'-38','-36')
WHERE surveydatedaysoffset = -36;

UPDATE action.actionrule
SET name = replace(name,'+8','+9')
,   description = replace(description,'+8','+9')
WHERE surveydatedaysoffset = 9;

UPDATE action.actionrule
SET name = replace(name,'+16','+17')
,   description = replace(description,'+16','+17')
WHERE surveydatedaysoffset = 17;

UPDATE action.actionrule
SET name = replace(name,'+1','+2')
,   description = replace(description,'+1','+2')
WHERE surveydatedaysoffset = 2;

UPDATE action.actionrule
SET name = replace(name,'+4','+8')
,   description = replace(description,'+4','+8')
WHERE surveydatedaysoffset = 8;