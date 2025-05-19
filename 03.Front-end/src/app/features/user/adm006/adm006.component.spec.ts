import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ADM006Component } from './adm006.component';

describe('ADM006Component', () => {
  let component: ADM006Component;
  let fixture: ComponentFixture<ADM006Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ADM006Component ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ADM006Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
