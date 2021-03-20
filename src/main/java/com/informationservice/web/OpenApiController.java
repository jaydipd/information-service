package com.informationservice.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.informationservice.dtos.OpenApiDTO;
import com.informationservice.dtos.WeatherDTO;
import com.informationservice.exceptionpkg.DataNotFoundException;
import com.informationservice.model.OpenApi;
import com.informationservice.model.Weather;
import com.informationservice.repositories.OpenApiRepository;
import com.informationservice.utility.OpenApiObjectConversionUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")

public class OpenApiController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OpenApiRepository openApiRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${open.api.uri}")
    private String openApiUri;


    @PostMapping(value = "/")
    public ResponseEntity<String> create(@RequestBody OpenApiDTO openApiDTO) throws Exception {

        OpenApi openApi = openApiRepository.save(OpenApiObjectConversionUtility.openApiDtoToEntity(openApiDTO));
        return ResponseEntity.ok(objectMapper.writeValueAsString(openApi));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OpenApi> findById(@PathVariable(name = "id") String id) throws Exception {
        Optional<OpenApi> openApi = openApiRepository.findById(id);
        if (!openApi.isPresent()) {
            throw new DataNotFoundException("Data not found: " + id);
        }
        return new ResponseEntity<>(openApi.get(), HttpStatus.OK);
    }

    @GetMapping("/")
    public String findAll() throws Exception {
        List<OpenApi> openApi = (List<OpenApi>) openApiRepository.findAll();
        return objectMapper.writeValueAsString(openApi);
    }


    @GetMapping("/city/{city}/{apiId}")
    public String getDataFromOpenWeatherApi(@PathVariable(name = "city") String city, @PathVariable(name = "apiId") String apiId) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(openApiUri)
                .queryParam("q", city)
                .queryParam("appid", apiId);
        ResponseEntity<String> response = restTemplate.getForEntity(uriBuilder.toUriString(), String.class);
        if (!(response.getStatusCode() == HttpStatus.OK)) {
            throw new DataNotFoundException("problem fetching data: " + city);
        }
        return response.getBody();
    }

    @GetMapping("/findByName/{name}")
    public List<OpenApi> findByName(@PathVariable(name = "name") String name) {
        Optional<List<OpenApi>> optionalListOfOpenApi = openApiRepository.findByName(name);
        if (!optionalListOfOpenApi.isPresent()) {
            throw new DataNotFoundException("Data not found for: " + name);
        }
        return optionalListOfOpenApi.get();
    }

    @PatchMapping("/updateWeather/openApi/{openApiId}")
    public OpenApi updateWeather(@PathVariable(name = "openApiId") String openApiId, @RequestBody WeatherDTO weatherDTO) {
        Optional<OpenApi> optionalOpenApi = openApiRepository.findById(openApiId);
        if (!optionalOpenApi.isPresent()) {
            throw new DataNotFoundException("openApi not found:" + openApiId);
        }
        OpenApi existingOpenApi = optionalOpenApi.get();
        if (!Objects.nonNull(existingOpenApi.getWeather())) {
            throw new DataNotFoundException("problem updating weather information as weather object is null");
        }
        existingOpenApi.setWeather(existingOpenApi.getWeather().stream().map(w -> transformWeather(w, getWeatherEntity(weatherDTO))).collect(Collectors.toList()));
        return openApiRepository.save(existingOpenApi);

    }

    public Weather getWeatherEntity(WeatherDTO weatherDTO) {
        return Weather
                .builder()
                .id(weatherDTO.getId())
                .description(weatherDTO.getDescription())
                .icon(weatherDTO.getIcon())
                .main(weatherDTO.getMain())
                .build();
    }

    public Weather transformWeather(Weather existingWeather, Weather requestedWeather) {
        return existingWeather.getId().equals(requestedWeather.getId()) ? requestedWeather : existingWeather;
    }
}
