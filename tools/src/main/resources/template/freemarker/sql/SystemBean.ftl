/**
 * @author ${author}
 */
package ${projectPath}.${data.functionName}.model.bean;

<#assign imp1="">
<#assign imp2="">
<#assign imp1=imp1+"java.sql.PreparedStatement|">
<#assign imp1=imp1+"java.sql.SQLException|">
<#if data.hasDate>
<#assign imp1=imp1+"java.util.Date|">
	<#assign hasJavaClass=true>
</#if>
<#if data.hasList>
<#assign imp1=imp1+"java.util.List|">
</#if>
<#if !data.mainKey.onlyField>
<#assign imp1=imp1+"java.util.LinkedHashMap|">
<#assign imp1=imp1+"java.util.Map|">
<#elseif data.hasMap>
<#assign imp1=imp1+"java.util.Map|">
</#if>
<#assign hasIndexList=false>
<#assign hasId=false>
<#list data.fields as field>
	<#if (field.indexList?size &gt; 1)>
		<#assign hasIndexList=true>
	</#if>
	<#if (field.uuid || field.incrementKey)>
		<#assign hasId=true>
	</#if>
	<#if (hasIndexList && hasId)>
		<#break>
	</#if>
</#list>
<#assign imp2=imp2+"com.tfzzh.core.annotation.DontShow|">
<#assign imp2=imp2+"com.tfzzh.log.jar.bean.BaseLogBean|">
<#assign imp2=imp2+"com.tfzzh.model.dao.annotation.DataField|">
<#assign imp2=imp2+"com.tfzzh.model.dao.annotation.DataTable|">
<#if hasId>
<#assign imp2=imp2+"com.tfzzh.model.dao.annotation.IdField|">
</#if>
<#assign imp2=imp2+"com.tfzzh.model.dao.annotation.KeyIndex|">
<#if hasIndexList>
<#assign imp2=imp2+"com.tfzzh.model.dao.annotation.KeyIndexs|">
</#if>
<#if hasId>
<#assign imp2=imp2+"com.tfzzh.model.dao.tools.IdFieldEnum|">
</#if>
<#assign imp2=imp2+"com.tfzzh.model.dao.tools.KeyIndexEnum|">
<#if imp1?length&gt;0>
	<#list imp1?split("|")?sort as im>
		<#if (im?length>0)>
import ${im};
		</#if>
	</#list>

</#if>
<#list imp2?split("|")?sort as im>
	<#if (im?length>0)>
import ${im};
	</#if>
</#list>

/**
 * ${data.desc}数据实体Bean
 * 
 * @author ${data.createAuthor}
 * @dateTime ${data.createDate}
<#if data.lastChangeAuthor?exists>
 * @lastChangeAuthor ${data.lastChangeAuthor}
</#if>
<#if data.lastChangeDate?exists>
 * @lastChangeDate ${data.lastChangeDate}
</#if>
 */
@DataTable(tableName = "${data.datatableName}", desc = "${data.desc}表")
public final class ${data.proName}${data.suffix} extends BaseLogBean {

	/**
	 * @author ${author}
	 */
	private static final long serialVersionUID = ${data.id?c}L;

	/**
	 * 对象ID
	 * 
	 * @author ${author}
	 */
	public static final Long OBJECT_ID = ${data.id?c}L;

	/**
	 * insertSql的头部
	 * 
	 * @author ${author}
	 */
	private static final String insertSqlHead = "INSERT INTO `";

	/**
	 * insertSql的主干
	 * 
	 * @author ${author}
	 */
	private static final String insertSqlBody = "` (<#assign isFirst=true><#list data.fields as f><#if !f.incrementKey><#if isFirst><#assign isFirst=false><#else>,</#if>`${f.datafieldName}`</#if></#list>) VALUES (<#assign isFirst=true><#list data.fields as f><#if !f.incrementKey><#if isFirst><#assign isFirst=false><#else>,</#if>?</#if></#list>);";

	/**
	 * 版本号
	 * 
	 * @author ${author}
	 */
	private int verId = 0;
<#assign size=data.fields?size>
<#assign index=0>
<#list data.fields as field>
	<#assign index=index+1>

	/**
	<#assign ind = 0>
	<#list field.descs as com>
		<#if ind == 0>
			<#assign ind = 1>
	 * ${com}<#if field.descs?size != 1>：<br /></#if>
		<#else>
	 * ${com}；<br />
		</#if>
	</#list>
	 * 
	 * @author ${field.createAuthor}
	 * @dateTime ${field.createDate}
	<#if field.lastChangeAuthor?exists>
	 * @lastChangeAuthor ${field.lastChangeAuthor}
	</#if>
	<#if field.lastChangeDate?exists>
	 * @lastChangeDate ${field.lastChangeDate}
	</#if>
	 */
	<#if (field.indexList?size!=0)>
		<#if field.indexList?size==1>
			<#assign fki=field.indexList[0]>
	@KeyIndex(<#if fki.name??>value = KeyIndexEnum.${fki.type.name()}, name = "${fki.name}", count = ${fki.max}, index = ${fki.index}<#else>KeyIndexEnum.${fki.type.name()}</#if>)
		<#else>
	@KeyIndexs({ <#list field.indexList as fki><#if (fki_index!=0)>, </#if>@KeyIndex(<#if fki.name??>value = KeyIndexEnum.${fki.type.name()}, name = "${fki.name}", count = ${fki.max}, index = ${fki.index}<#else>KeyIndexEnum.${fki.type.name()}</#if>)</#list> })
		</#if>
	</#if>
	<#if field.uuid>
	@IdField(IdFieldEnum.UUID)
	</#if>
	<#if field.incrementKey>
	@IdField(IdFieldEnum.Increment)
	</#if>
	@DataField(fieldName = "${field.datafieldName}", fieldType = "${field.datafieldType.typeName}"<#if (field.length != "" && field.length != "0")><#assign fls=field.length?split(",")><#if (fls?size==2)>, precision = ${fls[0]}, scale = ${fls[1]}<#else>, length = ${field.length}</#if></#if><#if field.hasDef()>, defValue = "${field.default}"</#if><#if field.canNull>, canNull = true</#if>, desc = "${field.desc}", index = ${index})
	private <#if field.datafieldType.classFieldType.typeName != "Map">${field.datafieldType.classFieldType.typeName}<#else>Map<Object, Object></#if> ${field.name};
</#list>

	/**
	 * 得到对象Id
	 * 
	 * @author ${author}
	 * @return 对象Id
	 */
	@Override
	public Long getObjectId() {
		return ${data.proName}${data.suffix}.OBJECT_ID;
	}

	/**
	 * 得到版本号
	 * 
	 * @author ${author}
	 * @return the verId
	 */
	public int getVerId() {
		return this.verId;
	}

	/**
	 * 设置版本号
	 * 
	 * @author ${author}
	 * @param verId the verId to set
	 */
	public void setVerId(final int verId) {
		this.verId = verId;
	}
<#list data.fields as field>

	/**
	<#assign ind = 0>
	<#list field.descs as com>
		<#if ind == 0>
			<#assign ind = 1>
	 * 得到${com}<#if field.descs?size != 1>：<br /></#if>
		<#else>
	 * ${com}；<br />
		</#if>
	</#list>
	 * 
	 * @author ${field.createAuthor}
	 * @dateTime ${field.createDate}
	<#if field.lastChangeAuthor?exists>
	 * @lastChangeAuthor ${field.lastChangeAuthor}
	</#if>
	<#if field.lastChangeDate?exists>
	 * @lastChangeDate ${field.lastChangeDate}
	</#if>
	 * @return the ${field.name}
	 */
	public <#if field.datafieldType.classFieldType.typeName != "Map">${field.datafieldType.classFieldType.typeName}<#else>Map<Object, Object></#if> get${field.proName}() {
		return this.${field.name};
	}

	/**
	<#assign ind = 0>
	<#list field.descs as com>
		<#if ind == 0>
			<#assign ind = 1>
	 * 设置${com}<#if ((field.datafieldType.typeName?index_of("int")!=-1) && field.unsigned && !field.incrementKey)>（可进行如果是负值则更新为增量的处理）</#if><#if field.descs?size != 1>：<br /></#if>
		<#else>
	 * ${com}；<br />
		</#if>
	</#list>
	 * 
	 * @author ${field.createAuthor}
	 * @dateTime ${field.createDate}
	<#if field.lastChangeAuthor?exists>
	 * @lastChangeAuthor ${field.lastChangeAuthor}
	</#if>
	<#if field.lastChangeDate?exists>
	 * @lastChangeDate ${field.lastChangeDate}
	</#if>
	 * @param ${field.name} the ${field.name} to set
	 */
	public void set${field.proName}(final <#if field.datafieldType.classFieldType.typeName != "Map">${field.datafieldType.classFieldType.typeName}<#else>Map<Object, Object></#if> ${field.name}) {
		this.${field.name} = ${field.name};
	}
</#list>

	/**
	 * 得到表名称
	 * 
	 * @author ${data.createAuthor}
	 * @dateTime ${data.createDate}
	 * @return 所相关表名称
	 */
	@DontShow
	@Override
	public String getTableName() {
		if (null == super.tableName) {
			return "${data.datatableName}";
		} else {
			return super.tableName;
		}
	}

	/**
	 * 得到自增字段的类型
	 * 
	 * @author ${author}
	 * @return ${data.incrementKey.type.objectTypeName}类型字段
	 * @see com.tfzzh.model.bean.BaseEntityBean#getIncrementKeyType()
	 */
	@Override
	public Class<${data.incrementKey.type.objectTypeName}> getIncrementKeyType() {
<#if data.incrementKey??>
		return ${data.incrementKey.type.objectTypeName}.class;
<#else>
		return null;
</#if>
	}

	/**
	 * 得到新增数据用SQL
	 * 
	 * @author ${author}
	 * @return 新增数据用SQL
	 * @see com.tfzzh.log.jar.bean.BaseLogBean#getInsertSql()
	 */
	@Override
	public String getInsertSql() {
		final String tn = this.getTableName();
		final StringBuilder sb = new StringBuilder(${data.proName}${data.suffix}.insertSqlHead.length() + tn.length() + ${data.proName}${data.suffix}.insertSqlBody.length());
		sb.append(${data.proName}${data.suffix}.insertSqlHead);
		sb.append(tn);
		sb.append(${data.proName}${data.suffix}.insertSqlBody);
		return sb.toString();
	}

	/**
	 * 将属性值放入到ps中
	 * 
	 * @author ${author}
	 * @param ps 目标ps
	 * @throws SQLException 抛
	 * @see com.tfzzh.log.jar.bean.BaseLogBean#putAttrToPreparedStatement(java.sql.PreparedStatement)
	 */
	@Override
	public void putAttrToPreparedStatement(final PreparedStatement ps) throws SQLException {
		int i = 1;
<#list data.fields as f>
	<#if !f.incrementKey>
		ps.set${f.type.toSocketTypeName()}(i++, this.${f.name});
	</#if>
</#list>
	}
}
