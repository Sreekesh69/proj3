package com.example.hicet.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.hicet.model.Tutorial;
import com.example.hicet.repository.TutorialRepository;

@RestController
@RequestMapping("/api")
public class TutorialController{

    @Autowired
    TutorialRepository tutorialRepository;

    @GetMapping("/show_all")

    public List<Tutorial> getAllTutorials(){
        return (List<Tutorial>) tutorialRepository.findAll();
    }

    @PostMapping("/create")
    public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial){
        Tutorial _tutorial= tutorialRepository
        .save(new Tutorial(tutorial.getFirstname(),tutorial.getLastname(),tutorial.getPassword()));

        return new ResponseEntity<Tutorial>(_tutorial, HttpStatus.CREATED);
    }
    //return new ResponseEntity<> (_tutorial.HttpStatus.CREATED)

    @DeleteMapping("/delete_all")
    public ResponseEntity<HttpStatus> deleteAllTutorial(){
        tutorialRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PutMapping("/insert/{id}")
    public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") Long id,@RequestBody Tutorial tutorial){
        Optional<Tutorial> __tutorial = tutorialRepository.findById(id);
        if(__tutorial.isPresent()){
            Tutorial _tutorial = __tutorial.get();
            _tutorial.setFirstname(tutorial.getFirstname());
            _tutorial.setLastname(tutorial.getLastname());
            _tutorial.setPassword(tutorial.getPassword());

            return new ResponseEntity<> (tutorialRepository.save(_tutorial),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        }
    }
    
    //define getsortDirection
    private Sort.Direction getSortDirection(String sort){
        if(sort.equals("asc")){
            return Sort.Direction.ASC;
        }
        else if(sort.equals("desc")){
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;

    }

    @GetMapping("/paginationandsorting")
    public ResponseEntity<Map<String,Object>> getAllTutorials(
        @RequestParam(defaultValue = "0")int page,
        @RequestParam(defaultValue = "5")int size,
        @RequestParam(defaultValue = "id,asc") String[] sort
        ){
            List<Order> order = new ArrayList<Order>();

            if(sort[0].contains(",")){
                for(String sortOrdet : sort){
                    String[] _sort = sortOrdet.split(",");
                    order.add(new Order(getSortDirection(_sort[1]),_sort[0]));
                }
            }
            else{
                order.add(new Order(getSortDirection(sort[1]),sort[0]));
            }
            List<Tutorial> tutorials = new ArrayList<Tutorial>();
            Pageable pagingSort = PageRequest.of(page , size , Sort.by(order));
            
            Page<Tutorial> pageTuts;
            pageTuts = tutorialRepository.findAll(pagingSort);

            tutorials = pageTuts.getContent();

            Map<String, Object> respone =new HashMap<>();
            response.put("currentPage",pageTuts.getNumber());
            response.put("pageSize", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalElements());

            return new ResponseEntity<>(response, HttpStatus.OK);

    }










}