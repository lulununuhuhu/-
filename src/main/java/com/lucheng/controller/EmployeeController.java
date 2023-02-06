package com.lucheng.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucheng.common.R;
import com.lucheng.constants.SystemConstant;
import com.lucheng.domain.Employee;
import com.lucheng.service.EmployeeService;
import com.lucheng.vo.EmployeeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Response;

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {
    @Resource
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<EmployeeVo> login(HttpServletRequest request, @RequestBody Employee employee){
        if(!StringUtils.hasText(employee.getUsername()))
            return R.error(SystemConstant.USERNAME_EMPTY);
        if(!StringUtils.hasText(employee.getPassword()))
            return R.error(SystemConstant.PASSWORD_EMPTY);
        return employeeService.login(request,employee);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //删掉对应的session
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @PostMapping("")
    public R<String> addEmployee(@RequestBody Employee employee){
        return employeeService.add(employee);
    }

    @GetMapping("/page")
    public R<Page> queryEmployee(Integer page,Integer pageSize,String name){
        log.info("page = {}, pageSize = {},name = {}",page,pageSize,name);
        return employeeService.queryEmployeeByPage(page,pageSize,name);
    }


    @PutMapping()
    public R<String> changeStatus(@RequestBody Employee employee){
        log.info("当前线程id是:"+Thread.currentThread().getId());
        return employeeService.updateEmployee(employee);
    }

    @GetMapping("/{id}")
    public R<Employee> getEmployeeById(@PathVariable("id") Long employeeId){
        Employee employee = employeeService.getById(employeeId);
        if(ObjectUtils.isEmpty(employee)){
            return R.error("查询失败");
        }else {
            return R.success(employee);
        }
    }
}
