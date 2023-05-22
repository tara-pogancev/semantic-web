import { Component } from '@angular/core';
import { QueryService } from 'src/app/service/query-service.service';

@Component({
  selector: 'main-screen',
  templateUrl: './main-screen.component.html',
  styleUrls: ['./main-screen.component.scss'],
})
export class MainScreenComponent {
  public isLoading: Boolean = false;
  public promptText: String = '';
  public responseText = 'Hi! Im happy to assist you today!';
  public errorResponse: Boolean = false;

  constructor(private queryService: QueryService) {}

  performQuery() {
    if (this.promptText.trim() != '') {
      this.isLoading = true;
      this.queryService.runQuery(this.promptText).subscribe((data) => {
        this.isLoading = false;
        console.log(data);
        this.responseText = data;
      });
    }
  }

  cardClicked(index: number) {
    switch (index) {
      case 0: {
        this.isLoading = true;
        this.promptText = 'What are the resources for course Semantic Web?';
        this.queryService.resourcesForCourseSample().subscribe((data) => {
          this.isLoading = false;
          console.log(data);
          this.responseText = data;
        });
        break;
      }
      case 1: {
        this.isLoading = true;
        this.promptText =
          'What are the resources for knowledge unit Basic Analysis?';
        this.queryService
          .resourcesForKnowledgeUnitSample()
          .subscribe((data) => {
            this.isLoading = false;
            console.log(data);
            this.responseText = data;
          });
        break;
      }
      case 2: {
        this.isLoading = true;
        this.promptText =
          'What courses does the author Jelena Jelic write for?';
        this.queryService.coursesForAuthorSample().subscribe((data) => {
          this.isLoading = false;
          console.log(data);
          this.responseText = data;
        });
        break;
      }
      case 3: {
        this.isLoading = true;
        this.promptText =
          'What are the courses and authors for learning outcome Describe security concerns in designing applications for use over wireless networks?';
        this.queryService
          .coursesAndAuthorsForLearningOutcomeSample()
          .subscribe((data) => {
            this.isLoading = false;
            console.log(data);
            this.responseText = data;
          });
        break;
      }
      case 4: {
        this.isLoading = true;
        this.promptText = 'What PDF documents are cited by Tara Pogancev?';
        this.queryService
          .documentsCitedByAuthorInFormatSample()
          .subscribe((data) => {
            this.isLoading = false;
            console.log(data);
            this.responseText = data;
          });
        break;
      }
    }
  }
}
