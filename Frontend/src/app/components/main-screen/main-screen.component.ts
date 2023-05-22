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
}
