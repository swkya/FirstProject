package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.AddressBook;
import com.itheima.reggie.mapper.AddressBookMapper;
import com.itheima.reggie.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
    @Autowired
    AddressBookMapper addressBookMapper;


    /*删逻辑除地址*/
    @Override
    public boolean UpdateByIds(long ids) {
        boolean result = addressBookMapper.updateByIds(ids);
        if (!result){
            return false;
        }
        return true;
    }
}