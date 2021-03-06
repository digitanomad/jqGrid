package com.digitanomad.gridtest.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.digitanomad.gridtest.dao.UserDao;
import com.digitanomad.gridtest.dto.UserDto;
import com.digitanomad.gridtest.service.UserExcelService;

@Service("UserServiceExcelUploadImpl")
public class UserServiceExcelUploadImpl implements UserExcelService {
	
	@Autowired
	UserDao userDao;

	@Override
	public boolean uploadUserExcel(MultipartFile file) {
		if (file == null || file.getSize() <= 0) {
			return false;
		}
		
		List<UserDto> userList = new ArrayList<UserDto>();
		
		try {
			Workbook wb = new HSSFWorkbook(file.getInputStream());
			Sheet sheet = wb.getSheetAt(0);
			
			int lastRowNum = sheet.getLastRowNum();
			
			for (int rowNum = 1; rowNum <= lastRowNum; rowNum++) {
				Row row = sheet.getRow(rowNum);
				UserDto userDto = new UserDto();
				
				userDto.setId((int)row.getCell(0, Row.CREATE_NULL_AS_BLANK).getNumericCellValue());
				userDto.setName(row.getCell(1, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
				userDto.setCompany(row.getCell(2, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
				userDto.setStartdate(row.getCell(3, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
				userDto.setCardnum(row.getCell(4, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
				
				System.out.println(userDto.toString());
				
				userList.add(userDto);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		userDao.insertUserList(userList);
		
		return true;
	}
}
