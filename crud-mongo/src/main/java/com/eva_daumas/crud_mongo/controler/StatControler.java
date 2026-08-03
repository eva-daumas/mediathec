package com.eva_daumas.crud_mongo.controler;

import com.eva_daumas.crud_mongo.model.Stat;
import com.eva_daumas.crud_mongo.repository.StatsRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StatControler {

    private final StatsRepository statsRepository;

    public StatControler(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @PostMapping("/createNote")
    public Stat addNote(@RequestBody Stat stat) {
        return statsRepository.save(stat);
    }

    @GetMapping("/getAllNotes")
    public Stat getNotesById(@RequestParam(name = "statId") String statId) {
        return statsRepository.findById(statId).get();

    }

}
