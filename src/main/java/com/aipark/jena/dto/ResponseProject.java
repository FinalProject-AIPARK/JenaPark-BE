package com.aipark.jena.dto;

import com.aipark.jena.domain.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ResponseProject {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class InitialProject {
        private Long projectId;

        public static InitialProject of(Project project) {
            return InitialProject.builder()
                    .projectId(project.getId())
                    .build();
        }
    }
}
