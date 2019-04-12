/**
 * @author Weijie Xu
 * @dateTime 2014-3-7 下午1:14:18
 */
package com.tfzzh.log.tools.tools;

import com.tfzzh.tools.data.bean.DataBeanTool;
import com.tfzzh.tools.data.template.SystemBeanTemplate;
import com.tfzzh.tools.data.tools.ToolBeanExcelPool;

/**
 * @author Weijie Xu
 * @dateTime 2014-3-7 下午1:14:18
 */
public class NewMakeFileForExcel {

	/**
	 * @author Weijie Xu
	 * @dateTime 2014-3-7 下午1:14:18
	 */
	public NewMakeFileForExcel() {
		// 暂时不用数据相关的处理
		this.makeData();
	}

	/**
	 * 进行模版生成，数据对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-7 下午1:14:18
	 */
	private void makeData() {
		for (final DataBeanTool t : ToolBeanExcelPool.getInstance().getAllData()) {
			new SystemBeanTemplate(t);
		}
	}
}
