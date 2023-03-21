let field = new Map();

field.set("bookName","书籍名称");

field.set("diseaseName","疾病描述");

field.set("herbName","草药名称");
field.set("herbSpellName","草药名拼音");
field.set("herbAlias","草药别名");
field.set("herbSource","草药来源");
field.set("herbOriginalForm","原形态");
field.set("herbGeography","生境分布");
field.set("herbCharacterSmell","草药性味");
field.set("herbFunction","功能主治");
field.set("herbUsageDosage","用法用量");
field.set("herbMaking","炮制方法");
field.set("herbExcerpt","摘录");

field.set("prescriptionName","药剂名称");
field.set("prescriptionCompose","药剂组成");
field.set("prescriptionProvenance","药剂出处");
field.set("prescriptionAttend","功能主治");
field.set("prescriptionUsageDosage","用法用量");
field.set("prescriptionPreparationFunction","制备方法");

let typeMap = new Map();
typeMap.set(0, "prescription");
typeMap.set(1, "herb");
typeMap.set(2, "disease");
typeMap.set(3, "book");
typeMap.set(4, "geography");