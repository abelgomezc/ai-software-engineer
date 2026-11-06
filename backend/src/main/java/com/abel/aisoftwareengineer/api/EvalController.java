package com.abel.aisoftwareengineer.api;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/eval")
public class EvalController {
    @PostMapping("/run")
    public Map<String,Object> run(){ return Map.of("status","not implemented"); }
    @GetMapping("/results")
    public Map<String,Object> results(){ return Map.of("results", java.util.List.of()); }
}
