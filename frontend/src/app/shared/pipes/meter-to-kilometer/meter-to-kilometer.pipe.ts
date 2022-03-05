import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'meterToKilometer'
})
export class MeterToKilometerPipe implements PipeTransform {

  transform(meters: number): number {
    const meterToKilometerUnit: number = 1000.0;    
    return (meters/meterToKilometerUnit);
  }

}
