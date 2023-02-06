package com.lucheng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lucheng.common.R;
import com.lucheng.domain.AddressBook;

/**
* @author lucheng
* @description 针对表【address_book(地址管理)】的数据库操作Service
* @createDate 2023-01-10 21:10:52
*/
public interface AddressBookService extends IService<AddressBook> {

    /**
     * 添加地址
     * @param addressBook
     * @return
     */
    public R<String> addAddressList(AddressBook addressBook);

    /**
     * 修改默认地址
     * @param addressBook
     * @return
     */
    public R<String> changeDefaultAddress(AddressBook addressBook);

    /**
     * 获取某个用户的默认地址
     * @return
     */
    public R<AddressBook> getDefaultAddress();
}
