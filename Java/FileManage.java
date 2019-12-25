package com.gzsas.filemanage.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gzsas.filemanage.dto.FileObj;
import com.gzsas.filemanage.dto.ResultObj;
import com.gzsas.filemanage.service.FileManegerService;
import com.gzsas.filemanage.util.CommonUtil;
import com.gzsas.filemanage.util.ConfigUtil;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	FileManegerService fileManegerService;

	// 上传文件路径
	private final String UPLOAD_FILE_PATH = ConfigUtil.getValue("uploadFilePath");

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@PostMapping("queryFileByBusinum.do")
	@ResponseBody
	public List<FileObj> queryFileByBusinum(String busiNum) {
		if (CommonUtil.hasValue(busiNum)) {
			return fileManegerService.queryFileByBusinum(busiNum);
		}
		return null;
	}

	@PostMapping("fileUpload.do")
	@ResponseBody
	public ResultObj upload(MultipartFile btnFile, @RequestParam String busiNum, @RequestParam String operId,
			String sysName) throws IOException {
		if (!btnFile.isEmpty()) {
			// 上传文件名
			String oriName = btnFile.getOriginalFilename();
			// 截取文件的扩展名(如.jpg)
			// String extName = oriName.substring(oriName.lastIndexOf("."));
			// 获取文件MIME类型，如image/jpeg等
			String fileType = btnFile.getContentType();
			// 获取文件的字节大小，单位为byte
			Long fileSize = btnFile.getSize();
			return new ResultObj(">>>"+oriName+fileType+fileSize);
		}
		if (!CommonUtil.hasValues(busiNum, operId)) {
			return new ResultObj("参数无效");
		}
		String path = UPLOAD_FILE_PATH;
		if (CommonUtil.hasValue(sysName)) {
			path += File.separator + sysName;
		}
		// 如果文件不为空，写入上传路径
		if (!btnFile.isEmpty()) {
			// 上传文件名
			String oriName = btnFile.getOriginalFilename();
			// 截取文件的扩展名(如.jpg)
			// String extName = oriName.substring(oriName.lastIndexOf("."));
			// 获取文件MIME类型，如image/jpeg等
			String fileType = btnFile.getContentType();
			// 获取文件的字节大小，单位为byte
			Long fileSize = btnFile.getSize();

			String filename = UUID.randomUUID().toString();
			File file = new File(path, filename);
			// 判断路径是否存在，如果不存在就创建一个
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			FileObj fileObj = new FileObj(oriName, Integer.parseInt(operId), sysName, fileSize, busiNum);
			// 保存到数据库
			int count = fileManegerService.saveFile(fileObj, fileType, file.getAbsolutePath());
			if (count == 1) {
				// 将上传文件保存到一个目标文件当中
				btnFile.transferTo(file);
				return new ResultObj(queryFileByBusinum(busiNum));
			}
			return new ResultObj("保存出错");
		} else {
			return new ResultObj("参数无效");
		}
	}

	@DeleteMapping("deleteUploadFile/{fileId}.do")
	@ResponseBody
	public ResultObj deleteUploadFile(@PathVariable String fileId) throws IOException {
		if (!CommonUtil.hasValues(fileId)) {
			return new ResultObj("参数无效");
		}

		return new ResultObj().isOK(fileManegerService.deleteFileByFileId(fileId), "删除失败");
	}

	@RequestMapping("getUploadFile/{fileId}.do")
	public void down(@PathVariable String fileId, HttpServletResponse response) throws IOException {
		FileObj fileObj = fileManegerService.queryFileByFileId(fileId);

		String fileName = fileObj.getFileName();
		String downloadFileName = URLEncoder.encode(fileName, "UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename=" + downloadFileName);
		response.setContentType("multipart/form-data");

		File file = new File(fileObj.getPath());
		InputStream bis = new BufferedInputStream(new FileInputStream(file));
		BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
		int len = 0;
		while ((len = bis.read()) != -1) {
			out.write(len);
			out.flush();
		}
		out.close();
		bis.close();
	}

	@PostMapping("downloadFile/{fileId}.do")
	public ResponseEntity<byte[]> download(@PathVariable String fileId) throws IOException {
		// 下载文件路径
		FileObj fileObj = fileManegerService.queryFileByFileId(fileId);
		String filename = fileObj.getFileName();
		File file = new File(fileObj.getPath());

		HttpHeaders headers = new HttpHeaders();
		// 下载显示的文件名，解决中文名称乱码问题
		String downloadFileName = new String(filename.getBytes("UTF-8"), "iso-8859-1");
		// 通知浏览器以attachment（下载方式）打开
		headers.setContentDispositionFormData("attachment", downloadFileName);
		// application/octet-stream ： 二进制流数据（最常见的文件下载）。
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
	}

}
