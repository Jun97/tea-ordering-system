import { Component, OnInit} from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component(
{
  templateUrl: './test-complete.component.htm'
})
export class TestCompleteComponent implements OnInit 
{
  mode:string = null;

  constructor(private route: ActivatedRoute)
  {
    
  }
  ngOnInit() 
  {
    this.route.params.subscribe(params => {
      this.mode = params['mode'];
    });
  }
}
