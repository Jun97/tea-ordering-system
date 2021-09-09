import { Component, ViewChild } from '@angular/core';
import { Router }  from '@angular/router';


@Component(
{
    templateUrl: './template-default.component.htm'
})
export class TemplateDefaultComponent 
{
  
}

@Component(
{
    templateUrl: './template-default-header.component.htm'
})
export class TemplateDefaultHeaderComponent
{
  readonly dashboard:any;

  constructor()
  {
  }
}

@Component(
{
    templateUrl: 'template-default-footer.component.htm'
})
export class TemplateDefaultFooterComponent 
{
        
}