import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompleteNotificationComponent } from './complete-notification.component';

describe('CompleteNotificationComponent', () => {
  let component: CompleteNotificationComponent;
  let fixture: ComponentFixture<CompleteNotificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CompleteNotificationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CompleteNotificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
