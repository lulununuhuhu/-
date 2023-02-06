package com.lucheng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucheng.common.R;
import com.lucheng.domain.AddressBook;
import com.lucheng.service.AddressBookService;
import com.lucheng.mapper.AddressBookMapper;
import com.lucheng.utils.ThreadLocalUtils;
import org.springframework.stereotype.Service;

/**
* @author lucheng
* @description 针对表【address_book(地址管理)】的数据库操作Service实现
* @createDate 2023-01-10 21:10:52
*/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
implements AddressBookService{

    @Override
    public R<String> addAddressList(AddressBook addressBook) {
        Long userId = (Long)ThreadLocalUtils.getCurrentUser();
        addressBook.setUserId(userId);
        save(addressBook);
        return R.success("添加地址信息成功");
    }

    @Override
    public R<String> changeDefaultAddress(AddressBook addressBook) {
        //先将原来的默认地址isDefault=1的数据改为0
        LambdaUpdateWrapper<AddressBook> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(AddressBook::getIsDefault,1);
        lambdaUpdateWrapper.set(AddressBook::getIsDefault,0);
        update(lambdaUpdateWrapper);
        //再从addressBook中根据id将新的地址对应的isDefault由1改为0
        lambdaUpdateWrapper.clear();
        lambdaUpdateWrapper.eq(AddressBook::getId,addressBook.getId());
        lambdaUpdateWrapper.set(AddressBook::getIsDefault,1);
        update(lambdaUpdateWrapper);
        return R.success("修改默认地址成功");
    }

    @Override
    public R<AddressBook> getDefaultAddress() {
        Long userId = (Long) ThreadLocalUtils.getCurrentUser();
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getIsDefault,1);
        queryWrapper.eq(AddressBook::getUserId,userId);
        AddressBook defaultAddress = getOne(queryWrapper);
        return R.success(defaultAddress);
    }
}
