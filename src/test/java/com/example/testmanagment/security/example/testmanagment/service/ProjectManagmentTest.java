package com.example.testmanagment.security.example.testmanagment.service;

import com.example.testmanagment.dto.ProjectDto;
import com.example.testmanagment.model.Project;
import com.example.testmanagment.model.User;
import com.example.testmanagment.repository.ProjectRepository;
import com.example.testmanagment.service.LogService;
import com.example.testmanagment.service.ProjectService;
import com.example.testmanagment.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.example.testmanagment.model.UserResponse;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;


public class ProjectManagmentTest {
    @InjectMocks
    private ProjectService projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private LogService logService;

    private ProjectDto testProject;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testProject = new ProjectDto();
        testProject.setId(1L);

        testProject.setName("vms");
        testProject.setDescription("tanım vms");


    }
    //test add
    @Test
    public void testAddProject_Success() {
        // Arrange
        when(projectRepository.findByName(testProject.getName())).thenReturn(null);
        Project savedProjectMock = new Project();
        savedProjectMock.setName(testProject.getName());
        savedProjectMock.setDescription(testProject.getDescription());
        when(projectRepository.save(any(Project.class))).thenReturn(savedProjectMock);

        // Act
        UserResponse response = projectService.addProject(testProject);

        // Assert
        assertNotNull(response); // Response nesnesinin null olmaması
        assertNotNull(response.getResult()); // Result alanının null olmaması
        assertFalse(response.getResult().isEmpty()); // Result’ın boş olmaması
        assertEquals("Added project successfully", response.getResult().get(0).getMessage());
    }

    //test delete
    @Test
    public void testDeleteProject_Success() {
        // Arrange
        Long projectId = 1L; // Silinecek projenin ID'si
        Project existingProject = new Project();
        existingProject.setId(projectId);
        existingProject.setName("Test Project");
        existingProject.setDescription("This is a test project.");
        existingProject.setIsdeleted(false); // Başlangıçta silinmemiş

        // Proje var
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));

        // Act
        UserResponse response = projectService.deleteProject(projectId);

        // Assert
        assertNotNull(response); // Response nesnesinin null olmaması
        assertNotNull(response.getResult()); // Result alanının null olmaması
        assertFalse(response.getResult().isEmpty()); // Result’ın boş olmaması
        assertTrue(response.getResult().getFirst().isStatus()); // İlk sonucun durumunun true olması
        assertEquals("Deleted project successfully", response.getResult().getFirst().getMessage()); // Mesajı kontrol et

        // Projenin silinmiş olarak işaretlendiğini kontrol et
        assertTrue(existingProject.isIsdeleted()); // isDeleted true olmalı

        // Projenin kaydedildiğine dair kontrol
        verify(projectRepository).save(existingProject); // Proje kaydedilmiş olmalı
    }

    //test update
    @Test
    public void testUpdateProject_Success() {
        // Arrange
        Long projectId = 1L; // Güncellenecek projenin ID'si
        Project existingProject = new Project();
        existingProject.setId(projectId);
        existingProject.setName("Original Project");
        existingProject.setDescription("This is the original project.");
        existingProject.setIsdeleted(false); // Proje silinmiş değil

        // Güncelleme yapılacak yeni proje verisi
        Project updatedProject = new Project();
        updatedProject.setId(projectId);
        updatedProject.setName("Updated Project"); // Yeni proje adı
        updatedProject.setDescription("Updated description."); // Yeni açıklama

        // Proje veritabanında var
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));

        // Act
        UserResponse response = projectService.updateProject(projectId, updatedProject);

        // Assert
        assertNotNull(response); // Response nesnesinin null olmaması
        assertNotNull(response.getResult()); // Result alanının null olmaması
        assertFalse(response.getResult().isEmpty()); // Result’ın boş olmaması
        assertTrue(response.getResult().getFirst().isStatus()); // İlk sonucun durumunun true olması
        assertEquals("Updated project successfully", response.getResult().getFirst().getMessage()); // Mesajı kontrol et

        // Güncellenen proje bilgilerini kontrol et
        assertEquals(updatedProject.getName(), existingProject.getName()); // Yeni isim kontrolü
        assertEquals(updatedProject.getDescription(), existingProject.getDescription()); // Yeni açıklama kontrolü

        // Projenin kaydedildiğine dair kontrol
        verify(projectRepository).save(existingProject); // Proje kaydedilmiş olmalı
    }
    @Test
    public void testGetAllProjects() {
        // Veritabanındaki örnek veriler
        Project project1 = new Project(); // Project sınıfını uygun şekilde başlatın
        project1.setId(1L); // Örnekte bir ID veriyoruz
        project1.setName("Project 1");

        Project project2 = new Project();
        project2.setId(2L);
        project2.setName("Project 2");

        List<Project> projectList = Arrays.asList(project1, project2);

        // Mock davranışları belirleme
        when(projectRepository.findAll()).thenReturn(projectList);

        // Servisin metodunu çağır ve sonucu kontrol et
        List<Project> result = projectService.getAllProjects();

        // Sonuçların beklenenlerle uyuşup uyuşmadığını kontrol et
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Project 1", result.get(0).getName());
        assertEquals("Project 2", result.get(1).getName());
    }

    @Test
    public void testGetProjectById_Found() {
        Long projectId = 1L;
        Project project = new Project(); // Project sınıfını uygun şekilde başlatın
        project.setId(projectId);
        project.setName("Project 1");

        // Mock davranışları belirleme
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        // Servisin metodunu çağır ve sonucu kontrol et
        Optional<Project> result = projectService.getProjectById(projectId);

        // Sonuçların beklenenlerle uyuşup uyuşmadığını kontrol et
        assertTrue(result.isPresent());
        assertEquals("Project 1", result.get().getName());
    }

    @Test
    public void testGetProjectById_NotFound() {
        Long projectId = 2L;

        // Mock davranışları belirleme
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // Servisin metodunu çağır ve sonucu kontrol et
        Optional<Project> result = projectService.getProjectById(projectId);

        // Sonuçların beklenenlerle uyuşup uyuşmadığını kontrol et
        assertFalse(result.isPresent());
    }
}
