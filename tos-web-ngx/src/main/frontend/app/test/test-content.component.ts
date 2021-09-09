import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TestService } from '../service/test.service';

import { TestModel } from '../model/test.model';
@Component(
{
  templateUrl: './test-content.component.htm'
})
export class TestContentComponent implements OnInit 
{
  tests:TestModel[] = [];

  constructor(private route: ActivatedRoute, private router: Router, private testService:TestService) 
  {
  }
  
  private fetch()
  {
    this.testService.findTestAll().subscribe(data=>{
      this.tests = <TestModel[]><any>data;
    });
  }
  
  remove(testId:number)
  {
    this.testService.deleteTestByTestId(testId).subscribe(res=>
    {
      this.router.navigate(['./complete', 'delete'], {relativeTo: this.route});
    });
  }
  
  ngOnInit()
  {
    this.fetch();
  }
}
