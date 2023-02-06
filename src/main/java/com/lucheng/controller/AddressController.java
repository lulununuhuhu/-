package com.lucheng.controller;

import com.lucheng.common.R;
import com.lucheng.domain.AddressBook;
import com.lucheng.service.AddressBookService;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户下单地址处理的相关接口
 */
@RestController
@RequestMapping("addressBook")
public class AddressController {

    @Resource
    private AddressBookService addressBookService;

    @PostMapping()
    public R<String> addAddress(@RequestBody AddressBook addressBook){
        if(ObjectUtils.isEmpty(addressBook)){
            return R.error("添加地址信息无效");
        }
        return addressBookService.addAddressList(addressBook);
    }

    @GetMapping("/list")
    public R<List<AddressBook>> listAddress(){
        return R.success(addressBookService.list());
    }

    @PutMapping("/default")
    public R<String> changeDefault(@RequestBody AddressBook addressBook){
        if(ObjectUtils.isEmpty(addressBook))
            return R.error("修改默认地址失败");
        return addressBookService.changeDefaultAddress(addressBook);
    }

    @GetMapping("/default")
    public R<AddressBook> getDefaultAddress(){
        return addressBookService.getDefaultAddress();
    }

    @GetMapping("/{id}")
    public R<AddressBook> showAddress(@PathVariable("id") Long addressId){
        return R.success(addressBookService.getById(addressId));
    }

    @PutMapping()
    public R<String> updateAddress(@RequestBody AddressBook addressBook){
        addressBookService.updateById(addressBook);
        return R.success("修改成功");
    }
}
