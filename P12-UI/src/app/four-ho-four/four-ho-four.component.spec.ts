import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FourHoFourComponent } from './four-ho-four.component';

describe('FourHoFourComponent', () => {
  let component: FourHoFourComponent;
  let fixture: ComponentFixture<FourHoFourComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FourHoFourComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FourHoFourComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
