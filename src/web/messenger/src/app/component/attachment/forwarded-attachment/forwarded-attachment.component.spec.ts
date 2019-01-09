import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ForwardedAttachmentComponent } from './forwarded-attachment.component';

describe('ForwardedAttachmentComponent', () => {
  let component: ForwardedAttachmentComponent;
  let fixture: ComponentFixture<ForwardedAttachmentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ForwardedAttachmentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ForwardedAttachmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
