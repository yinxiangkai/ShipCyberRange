package com.yongle.springboot.controller;


import com.yongle.springboot.common.Result;
import com.yongle.springboot.utils.OpenstackUtils;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yongle
 * @since 2023-02-07
 */
@RestController
@RequestMapping("/openstack")
public class OpenstackController {
      @GetMapping("/getnetwork")
        public Result test(){
                OpenstackUtils openstackutil=new OpenstackUtils();

                return openstackutil.getNetwork();
        }

        @GetMapping("/getimage")
        public Result getiamge() {
                OpenstackUtils openstackutil = new OpenstackUtils();
                return openstackutil.getimage();
        }
        @GetMapping("/getflavors")
        public Result getflavors() {
            OpenstackUtils openstackutil = new OpenstackUtils();
            return openstackutil.getflavors();
        }
        @GetMapping("/getserver")
        public Result getserver() {
            OpenstackUtils openstackutil = new OpenstackUtils();
            return openstackutil.getserver();
        }
        @GetMapping("/getports")
        public Result getports() {
            OpenstackUtils openstackutil = new OpenstackUtils();
            return openstackutil.getports();
        }
        @GetMapping("/createnetwork")
        public Result createnetwork() {
            OpenstackUtils openstackutil = new OpenstackUtils();
            return Result.success(openstackutil.createNetwork("test")) ;
        }


    // 新增或者更新


}

