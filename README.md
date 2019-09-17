# Elasticsearch
模块
elasticsearch查询

支持的功能
1 支持多字段模糊查询
2 支持多字段精确查询
3 支持多字段精确不包含查询
4 支持加权查询
5 支持范围查询
6 支持分页
7 支持指定返回字段

API调用说明
创建查询对象
1 默认方式，加载jar包中的elasticsearch.properties配置文件
Query query = ElasticsearchQueryBuilder.buildElasticsearchQuery().setDefault().build();
	/*附录1：Query是 Elasticsearch查询接口*/ 
	/*附录2：ElasticsearchQueryBuilder 查询构建器 */
	
2 指定配置文件方式，加载指定路径的配置文件
Query query = ElasticsearchQueryBuilder.buildElasticsearchQuery().setProperties("[elasticsearch.properties文件路径]").build();

3 调用API方式，动态指定查询对象需要的参数
Query query = ElasticsearchQueryBuilder.buildElasticsearchQuery()
	.setCusterName("ccmses")  //必填
	.setCusterNodes("ccmses1,ccmses2,ccmses3")  //必填
	.setCusterPort("9300") //必填
	.setMaxWaitMillis(1000L) //选填 默认-1L
	.build();



构建查询参数
创建java对象方式
1 创建对象：
	Param param = new Param("index","type");
	需要设置索引名称和类型名称，必填
2 设置分页信息：
	param.setPage(1);
	设置页数，默认第一页 选填
3 设置分页信息：
	param.setSize(20);
	设置页大小，默认十条，选填
4 设置排序信息：
	param.setSortField("Party_ID");
	设置排序字段，默认不排序 选填
5 设置排序信息：
	param.setSortOrder("asc");
	设置排序顺序，默认倒序，选填
6 设置结果集返回字段：
	param.setFieldName(new String[]{"PARTY_ID","CUST_NAME"});
	设置返回需要的字段，默认返回索引所有字段，选填
7 设置查询的条件：
1）精确匹配：
	TermsMatch terms = new TermsMatvh();//创建精确匹配对象
	terms.setMust(false);//等于或者不等于，默认等于
	terms.setFieldName("PARTY_ID");//设置匹配值
	terms.setValues(new String[]{"1","2","3"});//设置匹配值，多个值用数组封装，如果同时存在多个值的数组values和单个值的value，取values
	list.add(terms);//把匹配规则添加到list
2)不包含匹配
TermsMatch terms1 = new TermsMatch();//创建精确匹配对象
terms.setMust(false);//等于或者不等于 默认等于
terms.setFieldName("PARTY_ID");//设置匹配对象
terms.setValues(new String[]{"1","2",""3});//设置匹配值，多个值用数组封装，如果同时存在多个值的数组values和单个值的value，取values
list.add(terms1);//把匹配规则添加到list 
3）模糊匹配：
WildcardMatch wildcard = new WildcardMatch();//创建模糊匹配对象
wildcard.setFieldName("Party_ID");//设置匹配字段
wildcard.setValue("1");//设置匹配值
list.add(wildcard);//把匹配规则添加到list
4) 范围匹配：
RangeMatch range = new RandMatch();//创建范围查询对象
range.setFieldName("PARTY_ID");
range.setForm(1); //设置匹配值范围
range.setTo(5); //设置匹配值范围
range.setIncludeLower(false); //设置是否包含，默认包含，true:>=，false:>
range.setIncludeUpper(false); //设置是否包含，默认包含，true:<=，false:<
list.add(range);//把匹配规则添加到list
5) 加权匹配，加权值越大的优先显示：
BoostMatch boost = new BoostMatch();//创建加权匹配对象
boost.setFieldName("PARTY_ID");//设置匹配字段
boost.setValues(new String[]{"1","2","3"});//设置匹配值，多个值用数组封装，如果同时存在多个值的数组values和单个值的value，取values
boost.setBooset(2F);//设置加权值，默认1F
list.add(boost);//把匹配规则添加到list


JSON字符串方式
{
"index":"index",
"type":"type".
"page":1,
"size":10,
"sortField":"PARTY_ID",
"sortOrder":"desc",
"fieldNames":["PARTY_ID","CUST_NAME"],
"matches":[
	{
	"fieldName":"PARTY_ID",
	"values":["1","2"],
	"value":"1",
	"type":"TERMS",
	"must":true
	},
	{
	"fieldName":"PARTY_ID",
	"values":["1","2"],
	"value":"1",
	"type":"TERMS",
	"must":false
	},
	{
	"fieldName":"PARTY_ID",
	"values":"1",
	"type":"WILDCARD"
	},
	{
	"fieldName":"PARTY_ID",
	"from":"1",
	"to":"5",
	"includeLower":true,
	"includeUppper":true,
	"type":"RANGE"
	},
	{
	"fieldName":"PARTY_ID",
	"values":["1","2"],
	"value":"1",
	"boost":2,
	"type":"BOOST"
	},
]
}
