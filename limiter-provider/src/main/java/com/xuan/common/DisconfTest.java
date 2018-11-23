package com.xuan.common;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf.client.common.update.IDisconfUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Leo xuan
 * @date 2018/9/27
 */

@Service
@DisconfFile(filename="111.properties")
@DisconfUpdateService(classes = {DisconfTest.class})
public class DisconfTest implements IDisconfUpdate{

	String test;

	private static Logger log = LoggerFactory.getLogger(DisconfTest.class);

	@DisconfFileItem(name = "test", associateField = "test")
	public String getTest() {
		return test;
	}


	public void setTest(String test) {
		this.test = test;
	}

	@Override
	public void reload() throws Exception {
		InputStream normalInputSteam = null;
		InputStream errorInputSteam = null;
		String reloadCmd = "G:\\disconf\\reload.bat";

		Process reloadPc = Runtime.getRuntime().exec(reloadCmd);
		int reloadStatus = reloadPc.waitFor();
		log.info("修改LVS配置执行结果状态: "+reloadStatus);

		//0代表正确
		if(reloadStatus == 0){
			normalInputSteam = reloadPc.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(normalInputSteam));
			String normalMsg = "";
			log.info("lvs配置修改正确,输出日志信息:");
			while ((normalMsg = br.readLine()) != null) {
				log.info(normalMsg);
			}
			//修改成功，执行配置备份
			String backupCmd = "G:\\disconf\\success.bat";
			Process backupPc = Runtime.getRuntime().exec(backupCmd);
			int backupStatus = backupPc.waitFor();
			if(backupStatus == 0)
				log.error("lvs配置备份成功！脚本执行状态:"+backupStatus);
			else
				log.error("lvs配置备份失败！脚本执行状态:"+backupStatus);
		}
		else {
			//执行脚本报错，输出报错信息
			errorInputSteam = reloadPc.getErrorStream();
			BufferedReader errbr = new BufferedReader(new InputStreamReader(errorInputSteam));
			String errMsg = "";
			log.info("lvs配置修改错误,错误信息输出:");
			while ((errMsg = errbr.readLine()) != null) {
				log.error(errMsg);
			}

			//执行配置回滚
			String rollbackCmd = "G:\\disconf\\rollback.bat";
			Process rollbackPc = Runtime.getRuntime().exec(rollbackCmd);
			int rollbackStatus = rollbackPc.waitFor();
			log.info("lvs配置回滚！脚本执行状态:"+rollbackStatus);
		}
	}
}
