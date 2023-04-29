package com.example.demo.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Models.Batch;
import com.example.demo.Models.JsonResponse;
import com.example.demo.Repositories.BatchRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(path = "/api/batch")
public class batchController {

    private ObjectMapper map = new ObjectMapper();

    @Autowired
    BatchRepository batchRepository;

    // course section creation
    @PostMapping(value = "/create-batch", consumes = {"application/json", "application/xml"})
    private JsonResponse createClass(@RequestBody Batch batch){
        JsonResponse response = new JsonResponse();

        try {
            Batch create = batchRepository.save(batch);

            if(batch == null){
                response.status = "Error";
                response.message = "cannot create your schedule, internal server error.";

                return response;
            }

            response.status = "Success";
            response.message = create.toString();

        }catch(DataIntegrityViolationException e){

            response.status = "Error";
            response.message = "Batch already exist.";
            
        }
        return response;
    }

    @GetMapping(path = "/get-batches")
    private JsonResponse getBatch(){
        JsonResponse response = new JsonResponse();
        List<Batch> listBatch = batchRepository.findAll();

        if(listBatch.size() == 0){
            response.status = "Empty";
            response.message = "No class found.";

            return response;
        }

        String json;

        try {
            json = map.writeValueAsString(listBatch);
        } catch (JsonProcessingException e) {
            response.status = "Error";
            response.message = "Error processing your request.";

            return response;
        }

        response.status = "Success";
        response.message = json;

        return response;
    }

    @PutMapping(path = "/update-batch")
    private JsonResponse updateBatch(@RequestBody Batch batch){
        JsonResponse response = new JsonResponse();
        Batch updatedBatch = batchRepository.save(batch);

        if(updatedBatch == null){
            response.status = "Error";
            response.message = "Error processing your request.";

            return response;
        }

        String json;

        try {
            json = map.writeValueAsString(updatedBatch);
        } catch (JsonProcessingException e) {
            response.status = "Error";
            response.message = "Error processing your request.";

            return response;
        }

        response.status = "Success";
        response.message = json;

        return response;
    }

    @DeleteMapping(path = "/remove-batch/{id}")
    private JsonResponse removeBatch(@PathVariable int id){
        JsonResponse response = new JsonResponse();
        batchRepository.deleteById(id);

        response.status = "Success";
        response.message = "Successfully deleted a class.";

        return response;
    }

}
