package com.lucheng.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lucheng.common.R;
import com.lucheng.domain.Employee;
import com.lucheng.vo.EmployeeVo;

import javax.servlet.http.HttpServletRequest;

/**
* @author lucheng
* @description 针对表【employee(员工信息)】的数据库操作Service
* @createDate 2023-01-05 22:03:09
*/
public interface EmployeeService extends IService<Employee> {

    public R<EmployeeVo> login(HttpServletRequest request,Employee employee);

    /**
     * 添加员工
     * @param employee
     * @return
     */
    public R<String> add(Employee employee);

    /**
     * 分页查询员工信息
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    public R<Page> queryEmployeeByPage(Integer page, Integer pageSize, String name);

    /**
     * 改变员工角色状态/修改员工信息
     * @param employee
     * @return
     */
    public R<String> updateEmployee(Employee employee);
}
