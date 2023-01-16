import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChatServiceAppComponent } from './chat-service-app.component';

describe('ChatServiceAppComponent', () => {
  let component: ChatServiceAppComponent;
  let fixture: ComponentFixture<ChatServiceAppComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChatServiceAppComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChatServiceAppComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
