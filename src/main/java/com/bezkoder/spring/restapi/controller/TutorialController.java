package com.bezkoder.spring.restapi.controller;

import com.bezkoder.spring.restapi.model.Tutorial;
import com.bezkoder.spring.restapi.service.TutorialService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"http://localhost:8081"})
@RestController
@RequestMapping({"/api"})
public class TutorialController {

  @Autowired
  TutorialService tutorialService;

  @GetMapping({"/tutorials"})
  public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false) String title) {
    try {
      List<Tutorial> tutorials = new ArrayList<>();
      if (title == null) {
        List<Tutorial> found = this.tutorialService.findAll();
        Objects.requireNonNull(tutorials);
        found.forEach(tutorials::add);
      } else {
        List<Tutorial> found = this.tutorialService.findByTitleContaining(title);
        Objects.requireNonNull(tutorials);
        found.forEach(tutorials::add);
      }

      if (tutorials.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(tutorials, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>((MultiValueMap<String, String>) null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping({"/tutorials/{id}"})
  public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") long id) {
    Tutorial tutorial = this.tutorialService.findById(id);
    if (tutorial != null) {
      return new ResponseEntity<>(tutorial, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping({"/tutorials"})
  public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {
    try {
      Tutorial _tutorial = this.tutorialService.save(new Tutorial(tutorial.getTitle(), tutorial.getDescription(), false));
      return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>((MultiValueMap<String, String>) null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping({"/tutorials/{id}"})
  public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") long id, @RequestBody Tutorial tutorial) {
    Tutorial _tutorial = this.tutorialService.findById(id);
    if (_tutorial != null) {
      _tutorial.setTitle(tutorial.getTitle());
      _tutorial.setDescription(tutorial.getDescription());
      _tutorial.setPublished(tutorial.isPublished());
      return new ResponseEntity<>(this.tutorialService.save(_tutorial), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping({"/tutorials/{id}"})
  public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") long id) {
    try {
      this.tutorialService.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping({"/tutorials"})
  public ResponseEntity<HttpStatus> deleteAllTutorials() {
    try {
      this.tutorialService.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping({"/tutorials/published"})
  public ResponseEntity<List<Tutorial>> findByPublished() {
    try {
      List<Tutorial> tutorials = this.tutorialService.findByPublished(true);
      if (tutorials.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(tutorials, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // --------- New ping and version endpoints ------------

  @GetMapping("/ping")
  public ResponseEntity<String> ping() {
    return ResponseEntity.ok("Pong! API is running.");
  }

  @GetMapping("/version")
  public ResponseEntity<String> version() {
    return ResponseEntity.ok("Release 2025-R1");
  }
}
