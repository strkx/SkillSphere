package com.SkillSphere.micro_task_platform.controller;

import com.SkillSphere.micro_task_platform.entity.Skill;
import com.SkillSphere.micro_task_platform.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@CrossOrigin(origins = "http://localhost:3000")
public class SkillController {

    @Autowired
    private SkillRepository skillRepository;

    @PostMapping("/add")
    public Skill addSkill(@RequestBody Skill skill) {
        return skillRepository.save(skill);
    }

    @GetMapping("/all")
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    @DeleteMapping("/delete/{id}")
    public String deleteSkill(@PathVariable String id) {
        skillRepository.deleteById(id);
        return "Skill with id " + id + " deleted successfully!";
    }
}
