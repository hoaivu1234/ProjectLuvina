import { NgModule } from '@angular/core';
import { AppRoutingModule } from '../app-routing.module';
import { FooterComponent } from './component/common/footer.component';
import { HeaderComponent } from './component/common/header.component';
import { CompleteNotificationComponent } from './component/complete/complete-notification/complete-notification.component';

@NgModule({
  declarations: [ HeaderComponent, FooterComponent, CompleteNotificationComponent ],
  exports: [ HeaderComponent, FooterComponent ],
  imports: [
    AppRoutingModule
  ]
})
export class SharedModule {
}