package sennia.mohamed.portfolio.project;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.util.EnumUtils;
import sennia.mohamed.portfolio.Mapper;
import sennia.mohamed.portfolio.experience.ExperienceService;
import sennia.mohamed.portfolio.field.FieldDTO;
import sennia.mohamed.portfolio.field.FieldService;
import sennia.mohamed.portfolio.technologie.TechnologieService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class ProjectService {
private ProjectRepository projectRepository;
private ExperienceService experienceService;
private TechnologieService technologieService;
private FieldService fieldService;
private Mapper mapper;

    @Autowired
    private SessionFactory sessionFactory;
@Autowired
    public ProjectService(ProjectRepository projectRepository, Mapper mapper, ExperienceService experienceService,TechnologieService technologieService,FieldService fieldService) {
        this.projectRepository = projectRepository;
        this.mapper=mapper;
        this.experienceService=experienceService;
        this.fieldService=fieldService;
        this.technologieService=technologieService;
    }
    public  int addProject(ProjectDTO projectDTO){
   Project project=     this.mapper.toProject(projectDTO);

        project.setExperience(mapper.toExp(this.experienceService.getExperience(projectDTO.getIdExperience())));
project.setFields(projectDTO.getFields().stream().map(fieldDTO -> {return fieldService.getField(fieldDTO.getField_id());}).collect(Collectors.toSet()));
project.setTechnologies(projectDTO.getTechnologies().stream().map(technologieDTO -> {return technologieService.getTechnologie(technologieDTO.getId_technologie());}).collect(Collectors.toSet()));
  return this.projectRepository.save(project).getId_project();
    }
    public List<ProjectDTO> getProjects(){
    return this.projectRepository.findAll().stream().map(mapper::toProjectDto).collect(Collectors.toList());
    }
    public ProjectDTO getProject(int id){


    ProjectDTO projectDTO=this.projectRepository.findById(id).map(value->mapper.toProjectDto(value)).orElse(null);

    return projectDTO;
    }
    public void deleteProject(ProjectDTO projectDTO){

    this.projectRepository.delete(mapper.toProject(projectDTO));
    }
    public List<ProjectDTO> getProjectsByType(String type){
       for (ProjectType t:ProjectType.values()){
           if(type.equals(t.name())){
               return this.projectRepository.findByType(ProjectType.valueOf(type)).stream().map(mapper::toProjectDto).collect(Collectors.toList());

           }
       }return new ArrayList<ProjectDTO>();
    }
    public List<ProjectDTO>  filterProjects(Map<String,String> filters){

        String  type= (filters.get("type")!=null) ? filters.get("type"):"";
        List<String>  technologies= (filters.get("technologies")!=null) ? List.of(filters.get("technologies")):this.technologieService.getTechnologies().stream().map(value->value.getName()).collect(Collectors.toList());
        List<String>  fields= (filters.get("fields")!=null) ? List.of(filters.get("fields")):this.fieldService.getFields().stream().map(value->value.getFieldName()).collect(Collectors.toList());
        if(type.equals("")){

        return this.projectRepository.filterProjects(technologies,fields).stream().map(mapper::toProjectDto).collect(Collectors.toList());}
        else{
            return this.projectRepository.filterProjectsWithType(technologies,fields,ProjectType.valueOf(type)).stream().map(mapper::toProjectDto).collect(Collectors.toList());
        }

    }
}
