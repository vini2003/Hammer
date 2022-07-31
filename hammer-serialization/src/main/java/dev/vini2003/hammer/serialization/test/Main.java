package dev.vini2003.hammer.serialization.test;

import com.google.gson.JsonObject;
import dev.vini2003.hammer.serialization.api.common.node.Node;
import dev.vini2003.hammer.serialization.api.common.parser.BufParser;
import dev.vini2003.hammer.serialization.api.common.parser.JsonParser;
import dev.vini2003.hammer.serialization.api.common.parser.NbtParser;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Main {
	public record Salary(
			Type type,
			Currency currency,
			double amount
	) {
		public enum Type {
			MONTHLY,
			WEEKLY,
			DAILY;
			
			public static final Node<Type> NODE = Node.STRING.xmap(Type::valueOf, Type::name);
		}
		
		public enum Currency {
			USD,
			EUR,
			GBP;
			
			public static final Node<Currency> NODE = Node.STRING.xmap(Currency::valueOf, Currency::name);
		}
		
		public static final Node<Salary> NODE = Node.compound(
				Type.NODE.key("type").getter(Salary::type),
				Currency.NODE.key("currency").getter(Salary::currency),
				Node.DOUBLE.key("amount").getter(Salary::amount),
				Salary::new
		);
	}
	
	public record Job(
			Degree degree,
			Salary salary
	) {
		public enum Degree {
			NONE,
			BACHELORS,
			DOCTORS,
			MASTERS;
			
			public static final Node<Degree> NODE = Node.STRING.xmap(Degree::valueOf, Degree::name);
		}
		
		public static final Node<Job> NODE = Node.compound(
				Degree.NODE.key("degree").getter(Job::degree),
				Salary.NODE.key("salary").getter(Job::salary),
				Job::new
		);
	}
	
	public record Person(
			String name,
			int age,
			Job job
	) {
		public static final Node<Person> NODE = Node.compound(
				Node.STRING.key("name").getter(Person::name),
				Node.INTEGER.key("age").getter(Person::age),
				Job.NODE.key("job").getter(Person::job),
				Person::new
		);
	}
	
	public record Event(
			String name,
			String description,
			Date date
	) {
		public static final Node<Event> NODE = Node.compound(
				Node.STRING.key("name").getter(Event::name),
				Node.STRING.key("description").getter(Event::description),
				Node.LONG.key("date").xmap(Date::new, Date::getTime).getter(Event::date),
				Event::new
		);
	}
	
	public record Company(
			String name,
			Map<String, Person> employees,
			List<Event> events
	) {
		public static final Node<Company> NODE = Node.compound(
				Node.STRING.key("name").getter(Company::name),
				Node.map(Node.STRING, Person.NODE).key("employees").getter(Company::employees),
				Node.list(Event.NODE).key("events").getter(Company::events),
				Company::new
		);
	}
	
	public static final class Car {
		public static final Node<Car> NODE = Node.compound(
				Node.STRING.key("maker").getter(Car::getMaker).setter(Car::setMaker),
				Node.STRING.key("model").getter(Car::getModel).setter(Car::setModel),
				Car::new
		);
		
		public String maker;
		public String model;
		
		public Car(String maker, String model) {
			this.maker = maker;
			this.model = model;
		}
		
		public String getMaker() {
			return maker;
		}
		
		public void setMaker(String maker) {
			this.maker = maker;
		}
		
		public String getModel() {
			return model;
		}
		
		public void setModel(String model) {
			this.model = model;
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			Car car = (Car) o;
			return Objects.equals(maker, car.maker) && Objects.equals(model, car.model);
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(maker, model);
		}
	}
	
	public static void main(String[] args) {
		var company = new Company(
				"Microsoft",
				Map.of(
						"Joe Rogan", new Person("Joe Rogan", 54, new Job(Job.Degree.NONE, new Salary(Salary.Type.MONTHLY, Salary.Currency.USD, 8488553))),
						"Elon Musk", new Person("Elon Musk", 37, new Job(Job.Degree.MASTERS, new Salary(Salary.Type.MONTHLY, Salary.Currency.USD, 76453457))),
						"Jeff Bezos", new Person("Jeff Bezos", 14, new Job(Job.Degree.DOCTORS, new Salary(Salary.Type.MONTHLY, Salary.Currency.USD, 674564))),
						"Will Smith", new Person("Will Smith", 75, new Job(Job.Degree.BACHELORS, new Salary(Salary.Type.MONTHLY, Salary.Currency.USD, 745736))),
						"Mark Zuckerberg", new Person("Mark Zuckerberg", 34, new Job(Job.Degree.DOCTORS, new Salary(Salary.Type.MONTHLY, Salary.Currency.USD, 9552573)))
				),
				List.of(
						new Event("Public Speech", "A public speech by Joe Rogan.", new Date()),
						new Event("Public Speech", "A public speech by Elon Musk.", new Date()),
						new Event("Public Speech", "A public speech by Jeff Bezos.", new Date()),
						new Event("Public Speech", "A public speech by Will Smith.", new Date()),
						new Event("Public Speech", "A public speech by Mark Zuckerberg.", new Date())
				)
		);
		
		var companyNbt = new NbtCompound();
		var companyJson = new JsonObject();
		var companyBuf = new PacketByteBuf(Unpooled.buffer());
		
		Company.NODE.serialize(NbtParser.INSTANCE, company, companyNbt);
		Company.NODE.serialize(JsonParser.INSTANCE, company, companyJson);
		Company.NODE.serialize(BufParser.INSTANCE, company, companyBuf);
		
		var newCompanyFromNbt = Company.NODE.deserialize(NbtParser.INSTANCE, companyNbt);
		var newCompanyFromJson = Company.NODE.deserialize(JsonParser.INSTANCE, companyJson);
		var newCompanyFromBuf = Company.NODE.deserialize(BufParser.INSTANCE, companyBuf);
		
		assert company.equals(newCompanyFromNbt);
		assert company.equals(newCompanyFromJson);
		assert company.equals(newCompanyFromBuf);
		
		var carForNbt = new Car("Ford", "Mustang");
		var carForJson = new Car("Ford", "Mustang");
		var carForBuf = new Car("Ford", "Mustang");
		
		var otherCarForNbt = new Car("Tesla", "Model X");
		var otherCarForJson = new Car("Tesla", "Model X");
		var otherCarForBuf = new Car("Tesla", "Model X");
		
		var carNbt = new NbtCompound();
		var carJson = new JsonObject();
		var carBuf = new PacketByteBuf(Unpooled.buffer());
		
		Car.NODE.serialize(NbtParser.INSTANCE, carForNbt, carNbt);
		Car.NODE.serialize(JsonParser.INSTANCE, carForJson, carJson);
		Car.NODE.serialize(BufParser.INSTANCE, carForBuf, carBuf);
		
		Car.NODE.deserialize(NbtParser.INSTANCE, carNbt, otherCarForNbt);
		Car.NODE.deserialize(JsonParser.INSTANCE, carJson, otherCarForJson);
		Car.NODE.deserialize(BufParser.INSTANCE, carBuf, otherCarForBuf);
		
		assert carForNbt.equals(otherCarForNbt);
		assert carForJson.equals(otherCarForJson);
		assert carForBuf.equals(otherCarForBuf);
		
		System.out.println("Tests succeeded!");
	}
}
