package com.benx.appupdate;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.dongliu.apk.parser.ApkParser;
import net.dongliu.apk.parser.bean.ApkMeta;

import org.apache.commons.fileupload.DefaultFileItemFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.starit.core.dao.JdbcDAOImpl;

/**
 * @author Qi
 * 
 */
@Controller
@RequestMapping("appupdate")
public class AppUpdate {

	private final String APP_BASEPATH = "/home/wdli/EOS5_3_5_MOBILE/jboss-3.2.5/server/default/deploy/eos4jboss/default.war/androidRest/page/";

	@Autowired
	private JdbcDAOImpl jdbcDao;

	@RequestMapping
	public String index() {
		return "appUpdate";
	}

	/**
	 * 上传文件
	 * 
	 * @param req
	 * @param type
	 * @return
	 * @throws FileUploadException
	 */
	@RequestMapping(value = "upload", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> upload(HttpServletRequest req, @RequestParam
	String type) throws FileUploadException {
		Map<String, String> map = new HashMap<String, String>();
		System.out.println(type);

		// 文件上传类
		ServletFileUpload upload = new ServletFileUpload(
				new DefaultFileItemFactory());
		// 解决上传文件名的中文乱码
		// upload.setHeaderEncoding("UTF-8");

		if ("".equals(type) || !ServletFileUpload.isMultipartContent(req)) {
			// 按照传统方式获取数据
			return map;
		}

		String fileName = APP_BASEPATH + type;
		String packageName = null;
		String versionName = null;
		// 接收用户上传信息
		try {
			List<FileItem> items = upload.parseRequest(req);
			System.out.println("items.size>>>>>>" + items.size());
			// 遍历items
			for (FileItem item : items) {
				// 一般表单域
				if (item.isFormField()) {
					String name = item.getFieldName();
					String date = item.getString();
					System.out.println("isFormField(name: " + name + " date: "
							+ date);
				} else {
					// 若是文件域则把文件保存到d盘临时文件夹
					@SuppressWarnings("unused")
					String fieldName = item.getFieldName();
					// 上传的文件名
					String originalFilename = item.getName();
					if (originalFilename == null
							|| originalFilename.trim().equals("")) {
						continue;
					}
					// 得到上传文件的扩展名
					String fileExtName = originalFilename
							.substring(originalFilename.lastIndexOf(".") + 1);
					if (fileExtName.equalsIgnoreCase("ipa")) {
						fileName += ".ipa.TMP";
					}
					if (fileExtName.equalsIgnoreCase("apk")) {
						fileName += ".apk.TMP";
					}
					// 上传的文件类型
					String contentType = item.getContentType();
					// 上传的文件大小
					long sizeInBytes = item.getSize();

					System.out.println(originalFilename + ", contentType: "
							+ contentType + ", sizeInBytes: " + sizeInBytes);
					File file = new File(fileName);
					item.write(file);
					// 删除处理文件上传时生成的临时文件
					item.delete();
				}
			}

			if (fileName.endsWith(".apk.TMP")) {
				ApkParser apkFile = new ApkParser(new File(fileName));
				ApkMeta apkMeta = apkFile.getApkMeta();

				packageName = apkMeta.getPackageName();
				versionName = apkMeta.getVersionName();
				apkFile.close();
			}
			map.put("appVersion", versionName);
			map.put("packageName", packageName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 升级操作
	 * 
	 * @param appName
	 * @param appVersion
	 * @param upgradeContent
	 * @param keypass
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> update(@RequestParam
	String appName, @RequestParam
	String appVersion, @RequestParam
	String upgradeContent, @RequestParam
	String keypass) throws UnsupportedEncodingException {
		Map<String, String> result = new LinkedHashMap<String, String>();
		if (!"ustcinfo".equalsIgnoreCase(keypass)) {
			result.put("MSG", "error!");
			return result;
		}

		String ipaName = APP_BASEPATH + appName + ".ipa";
		String ipaNameTMP = APP_BASEPATH + appName + ".ipa.TMP";
		String ipaNameBak = APP_BASEPATH + appName + ".ipaV";

		String apkName = APP_BASEPATH + appName + ".apk";
		String apkNameTMP = APP_BASEPATH + appName + ".apk.TMP";
		String apkNameBak = APP_BASEPATH + appName + ".apkV";


		File apkFileTMP = new File(apkNameTMP);
		if (!apkFileTMP.exists()) {
			result.put("MSG", "重复提交？");
			return result;
		}
		
		// 获取旧版本号
		File apkFile = new File(apkName);
		try {
			ApkParser apkParser = new ApkParser(apkFile);
			ApkMeta apkMeta = apkParser.getApkMeta();
			apkParser.close();
			String oldAppVersionName = apkMeta.getVersionName();
			ipaNameBak += oldAppVersionName;
			apkNameBak += oldAppVersionName;
			result.put("getOldAppVersionName", oldAppVersionName);
		} catch (IOException e) {
			result.put("getOldAppVersionName", "error!");
			result.put("MSG", e.getMessage());
			return result;
		}

		// 备份旧版本，重命名为原文件名+V版本号，GZISAS.apkV1.0
		File ipaFile = new File(ipaName);
		if (ipaFile.exists()) {
			File ipaFileBak = new File(ipaNameBak);
			if (ipaFileBak.exists()) {
				ipaFileBak.delete();
			}
			boolean ipaRenameSuccess = ipaFile.renameTo(ipaFileBak);
			result.put("ipaBakSuccess", String.valueOf(ipaRenameSuccess));
		}
		if (apkFile.exists()) {
			File apkFileBak = new File(apkNameBak);
			if (apkFileBak.exists()) {
				apkFileBak.delete();
			}
			boolean apkRenameSuccess = apkFile.renameTo(apkFileBak);
			result.put("apkBakSuccess", String.valueOf(apkRenameSuccess));
		}

		// 替换原文件
		File ipaFileTMP = new File(ipaNameTMP);
		if (ipaFileTMP.exists()) {
			boolean ipaRenameSuccess = ipaFileTMP.renameTo(ipaFile);
			result.put("ipaReplaceSuccess", String.valueOf(ipaRenameSuccess));
		}
		boolean apkRenameSuccess = false;
		if (apkFileTMP.exists()) {
			apkRenameSuccess = apkFileTMP.renameTo(apkFile);
			result.put("apkReplaceSuccess", String.valueOf(apkRenameSuccess));
		}

		// 修改数据库提示升级
		if (apkRenameSuccess) {
			Map<String, String> paramMap = new HashMap<String, String>();
			String sql = null;
			paramMap.put("appversion", appVersion);
			paramMap.put("upgradecontent", new String(upgradeContent.getBytes("ISO-8859-1"), "GBK"));
			if (appName.equalsIgnoreCase("INTELL_GZISAS")) {
				sql = "UPDATE SGDD.APP_VERSION T SET T.EDITIONNUM = :appversion, T.UPDATECONTENT = :upgradecontent WHERE T.UPDATEPLATFORM IN ('android_new', 'ios_new')";
			}
			if (appName.equalsIgnoreCase("GZISAS")) {
				sql = "UPDATE SGDD.APP_VERSION T SET T.EDITIONNUM = :appversion, T.UPDATECONTENT = :upgradecontent WHERE T.UPDATEPLATFORM IN ('android', 'ios')";
			}
			boolean update = jdbcDao.update(sql, paramMap);
			result.put("updateSuccess", String.valueOf(update));
		}
		return result;
	}

	public static void main(String[] args) throws IOException {

		String fileName = "D:\\Users\\Qi\\Desktop\\GZISAS.apk";
		File file = new File(fileName);
		System.out.println(file.getAbsolutePath());
		ApkParser apkFile = new ApkParser(file);
		ApkMeta apkMeta = apkFile.getApkMeta();

		String packageName = apkMeta.getPackageName();
		String versionName = apkMeta.getVersionName();
		System.out.println(packageName);
		System.out.println(versionName);
	}
}
