import { Component, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'side-menu',
  templateUrl: './side-menu.component.html',
  styleUrls: ['./side-menu.component.scss']
})
export class SideMenuComponent {

  @Output() cardClicked: EventEmitter<number> = new EventEmitter<number>()

  selectCard(index: number) {
    this.cardClicked.emit(index)
  }

}
 