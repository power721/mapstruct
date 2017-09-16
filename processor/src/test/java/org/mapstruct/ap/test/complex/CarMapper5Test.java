/**
 * Copyright 2012-2017 Gunnar Morling (http://www.gunnarmorling.de/)
 * and/or other contributors as indicated by the @authors tag. See the
 * copyright.txt file in the distribution for a full listing of all
 * contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mapstruct.ap.test.complex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.mapstruct.ap.test.complex._target.CarDto;
import org.mapstruct.ap.test.complex._target.PersonDto;
import org.mapstruct.ap.test.complex.other.DateMapper;
import org.mapstruct.ap.test.complex.source.Car;
import org.mapstruct.ap.test.complex.source.Category;
import org.mapstruct.ap.test.complex.source.Person;
import org.mapstruct.ap.testutil.WithClasses;
import org.mapstruct.ap.testutil.api.CompilerTest;
import org.mapstruct.ap.testutil.runner.EclipseTemplateContextProvider;
import org.mapstruct.ap.testutil.runner.JdkTemplateContextProvider;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Filip Hrisafov
 */
@Extensions({
    @ExtendWith(JdkTemplateContextProvider.class),
    @ExtendWith(EclipseTemplateContextProvider.class)

})
@WithClasses({
    Car.class,
    CarDto.class,
    Person.class,
    PersonDto.class,
    CarMapper.class,
    Category.class,
    DateMapper.class
})
class CarMapper5Test {

    @CompilerTest
    void junit5Test() {

        //given
        Car car = new Car(
            "Morris",
            2,
            new GregorianCalendar( 1980, 0, 1 ).getTime(),
            new Person( "Bob" ),
            new ArrayList<Person>()
        );

        //when
        CarDto carDto = CarMapper.INSTANCE.carToCarDto( car );

        //then
        assertThat( carDto ).isNotNull();
        assertThat( carDto.getMake() ).isEqualTo( car.getMake() );
    }

    @CompilerTest
    void shouldMapReferenceAttribute() {
        //given
        Car car = new Car(
            "Morris",
            2,
            new GregorianCalendar( 1980, 0, 1 ).getTime(),
            new Person( "Bob" ),
            new ArrayList<Person>()
        );

        //when
        CarDto carDto = CarMapper.INSTANCE.carToCarDto( car );

        //then
        assertThat( carDto ).isNotNull();
        assertThat( carDto.getDriver() ).isNotNull();
        assertThat( carDto.getDriver().getName() ).isEqualTo( "Bob" );
    }

    @CompilerTest
    void shouldReverseMapReferenceAttribute() {
        //given
        CarDto carDto = new CarDto( "Morris", 2, "1980", new PersonDto( "Bob" ), new ArrayList<PersonDto>() );

        //when
        Car car = CarMapper.INSTANCE.carDtoToCar( carDto );

        //then
        assertThat( car ).isNotNull();
        assertThat( car.getDriver() ).isNotNull();
        assertThat( car.getDriver().getName() ).isEqualTo( "Bob" );
    }

    @CompilerTest
    void shouldMapAttributeWithCustomMapping() {
        //given
        Car car = new Car(
            "Morris",
            2,
            new GregorianCalendar( 1980, 0, 1 ).getTime(),
            new Person( "Bob" ),
            new ArrayList<Person>()
        );

        //when
        CarDto carDto = CarMapper.INSTANCE.carToCarDto( car );

        //then
        assertThat( carDto ).isNotNull();
        assertThat( carDto.getSeatCount() ).isEqualTo( car.getNumberOfSeats() );
    }

    @CompilerTest
    void shouldConsiderCustomMappingForReverseMapping() {
        //given
        CarDto carDto = new CarDto( "Morris", 2, "1980", new PersonDto( "Bob" ), new ArrayList<PersonDto>() );

        //when
        Car car = CarMapper.INSTANCE.carDtoToCar( carDto );

        //then
        assertThat( car ).isNotNull();
        assertThat( car.getNumberOfSeats() ).isEqualTo( carDto.getSeatCount() );
    }

    @CompilerTest
    void shouldApplyConverter() {
        //given
        Car car = new Car(
            "Morris",
            2,
            new GregorianCalendar( 1980, 0, 1 ).getTime(),
            new Person( "Bob" ),
            new ArrayList<Person>()
        );

        //when
        CarDto carDto = CarMapper.INSTANCE.carToCarDto( car );

        //then
        assertThat( carDto ).isNotNull();
        assertThat( carDto.getManufacturingYear() ).isEqualTo( "1980" );
    }

    @CompilerTest
    void shouldApplyConverterForReverseMapping() {
        //given
        CarDto carDto = new CarDto( "Morris", 2, "1980", new PersonDto( "Bob" ), new ArrayList<PersonDto>() );

        //when
        Car car = CarMapper.INSTANCE.carDtoToCar( carDto );

        //then
        assertThat( car ).isNotNull();
        assertThat( car.getManufacturingDate() ).isEqualTo( new GregorianCalendar( 1980, 0, 1 ).getTime() );
    }

    @CompilerTest
    void shouldMapIterable() {
        //given
        Car car1 = new Car(
            "Morris",
            2,
            new GregorianCalendar( 1980, 0, 1 ).getTime(),
            new Person( "Bob" ),
            new ArrayList<Person>()
        );
        Car car2 = new Car(
            "Railton",
            4,
            new GregorianCalendar( 1934, 0, 1 ).getTime(),
            new Person( "Bill" ),
            new ArrayList<Person>()
        );

        //when
        List<CarDto> dtos = CarMapper.INSTANCE.carsToCarDtos( new ArrayList<Car>( Arrays.asList( car1, car2 ) ) );

        //then
        assertThat( dtos ).isNotNull();
        assertThat( dtos ).hasSize( 2 );

        assertThat( dtos.get( 0 ).getMake() ).isEqualTo( "Morris" );
        assertThat( dtos.get( 0 ).getSeatCount() ).isEqualTo( 2 );
        assertThat( dtos.get( 0 ).getManufacturingYear() ).isEqualTo( "1980" );
        assertThat( dtos.get( 0 ).getDriver().getName() ).isEqualTo( "Bob" );

        assertThat( dtos.get( 1 ).getMake() ).isEqualTo( "Railton" );
        assertThat( dtos.get( 1 ).getSeatCount() ).isEqualTo( 4 );
        assertThat( dtos.get( 1 ).getManufacturingYear() ).isEqualTo( "1934" );
        assertThat( dtos.get( 1 ).getDriver().getName() ).isEqualTo( "Bill" );
    }

    @CompilerTest
    void shouldReverseMapIterable() {
        //given
        CarDto car1 = new CarDto( "Morris", 2, "1980", new PersonDto( "Bob" ), new ArrayList<PersonDto>() );
        CarDto car2 = new CarDto( "Railton", 4, "1934", new PersonDto( "Bill" ), new ArrayList<PersonDto>() );

        //when
        List<Car> cars = CarMapper.INSTANCE.carDtosToCars( new ArrayList<CarDto>( Arrays.asList( car1, car2 ) ) );

        //then
        assertThat( cars ).isNotNull();
        assertThat( cars ).hasSize( 2 );

        assertThat( cars.get( 0 ).getMake() ).isEqualTo( "Morris" );
        assertThat( cars.get( 0 ).getNumberOfSeats() ).isEqualTo( 2 );
        assertThat( cars.get( 0 ).getManufacturingDate() ).isEqualTo( new GregorianCalendar( 1980, 0, 1 ).getTime() );
        assertThat( cars.get( 0 ).getDriver().getName() ).isEqualTo( "Bob" );

        assertThat( cars.get( 1 ).getMake() ).isEqualTo( "Railton" );
        assertThat( cars.get( 1 ).getNumberOfSeats() ).isEqualTo( 4 );
        assertThat( cars.get( 1 ).getManufacturingDate() ).isEqualTo( new GregorianCalendar( 1934, 0, 1 ).getTime() );
        assertThat( cars.get( 1 ).getDriver().getName() ).isEqualTo( "Bill" );
    }

    @CompilerTest
    void shouldMapIterableAttribute() {
        //given
        Car car = new Car(
            "Morris",
            2,
            new GregorianCalendar( 1980, 0, 1 ).getTime(),
            new Person( "Bob" ),
            new ArrayList<Person>( Arrays.asList( new Person( "Alice" ), new Person( "Bill" ) ) )
        );

        //when
        CarDto dto = CarMapper.INSTANCE.carToCarDto( car );

        //then
        assertThat( dto ).isNotNull();

        assertThat( dto.getPassengers() ).hasSize( 2 );
        assertThat( dto.getPassengers().get( 0 ).getName() ).isEqualTo( "Alice" );
        assertThat( dto.getPassengers().get( 1 ).getName() ).isEqualTo( "Bill" );
    }

    @CompilerTest
    void shouldReverseMapIterableAttribute() {
        //given
        CarDto carDto = new CarDto(
            "Morris",
            2,
            "1980",
            new PersonDto( "Bob" ),
            new ArrayList<PersonDto>( Arrays.asList( new PersonDto( "Alice" ), new PersonDto( "Bill" ) ) )
        );

        //when
        Car car = CarMapper.INSTANCE.carDtoToCar( carDto );

        //then
        assertThat( car ).isNotNull();

        assertThat( car.getPassengers() ).hasSize( 2 );
        assertThat( car.getPassengers().get( 0 ).getName() ).isEqualTo( "Alice" );
        assertThat( car.getPassengers().get( 1 ).getName() ).isEqualTo( "Bill" );
    }

    @CompilerTest
    void shouldMapEnumToString() {
        //given
        Car car = new Car();
        car.setCategory( Category.CONVERTIBLE );
        //when
        CarDto carDto = CarMapper.INSTANCE.carToCarDto( car );

        //then
        assertThat( carDto ).isNotNull();
        assertThat( carDto.getCategory() ).isEqualTo( "CONVERTIBLE" );
    }

    @CompilerTest
    void shouldMapStringToEnum() {
        //given
        CarDto carDto = new CarDto();
        carDto.setCategory( "CONVERTIBLE" );
        //when
        Car car = CarMapper.INSTANCE.carDtoToCar( carDto );

        //then
        assertThat( car ).isNotNull();
        assertThat( car.getCategory() ).isEqualTo( Category.CONVERTIBLE );
    }
}
