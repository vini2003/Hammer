package dev.vini2003.hammer.serialization.test;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.vini2003.hammer.serialization.api.common.node.Node;
import dev.vini2003.hammer.serialization.api.common.parser.BufParser;
import dev.vini2003.hammer.serialization.api.common.parser.JsonParser;
import dev.vini2003.hammer.serialization.api.common.parser.NbtParser;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
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
			
			public static final Codec<Type> CODEC = Codec.STRING.xmap(Type::valueOf, Type::name);
		}
		
		public enum Currency {
			USD,
			EUR,
			GBP;
			
			public static final Node<Currency> NODE = Node.STRING.xmap(Currency::valueOf, Currency::name);
			
			public static final Codec<Currency> CODEC = Codec.STRING.xmap(Currency::valueOf, Currency::name);
		}
		
		public static final Node<Salary> NODE = Node.compound(
				Type.NODE.key("type").getter(Salary::type),
				Currency.NODE.key("currency").getter(Salary::currency),
				Node.DOUBLE.key("amount").getter(Salary::amount),
				Salary::new
		);
		
		public static final Codec<Salary> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						Type.CODEC.fieldOf("type").forGetter(Salary::type),
						Currency.CODEC.fieldOf("currency").forGetter(Salary::currency),
						Codec.DOUBLE.fieldOf("amount").forGetter(Salary::amount)
				).apply(instance, Salary::new)
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
			
			public static final Codec<Degree> CODEC = Codec.STRING.xmap(Degree::valueOf, Degree::name);
		}
		
		public static final Node<Job> NODE = Node.compound(
				Degree.NODE.key("degree").getter(Job::degree),
				Salary.NODE.key("salary").getter(Job::salary),
				Job::new
		);
		
		public static final Codec<Job> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						Degree.CODEC.fieldOf("degree").forGetter(Job::degree),
						Salary.CODEC.fieldOf("salary").forGetter(Job::salary)
				).apply(instance, Job::new)
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
		
		public static final Codec<Person> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						Codec.STRING.fieldOf("name").forGetter(Person::name),
						Codec.INT.fieldOf("age").forGetter(Person::age),
						Job.CODEC.fieldOf("job").forGetter(Person::job)
				).apply(instance, Person::new)
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
		
		public static final Codec<Event> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						Codec.STRING.fieldOf("name").forGetter(Event::name),
						Codec.STRING.fieldOf("description").forGetter(Event::description),
						Codec.LONG.fieldOf("date").xmap(Date::new, Date::getTime).forGetter(Event::date)
				).apply(instance, Event::new)
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
		
		public static final Codec<Company> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						Codec.STRING.fieldOf("name").forGetter(Company::name),
						Codec.unboundedMap(Codec.STRING, Person.CODEC).fieldOf("employees").forGetter(Company::employees),
						Codec.list(Event.CODEC).fieldOf("events").forGetter(Company::events)
				).apply(instance, Company::new)
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
		
		var companyLoadNbtForNode = new NbtCompound();
		Company.NODE.serialize(NbtParser.INSTANCE, company, companyLoadNbtForNode);
		var companyLoadNbtForCodec = new NbtCompound();
		Company.CODEC.encode(company, NbtOps.INSTANCE, companyLoadNbtForCodec);
		
		long nodeTime = 0L;
		var codecTime = 0L;
		
		for (var i = 0; i < 2_500_000; ++i) {
			var nodeNbt = new NbtCompound();
			var codecNbt = new NbtCompound();
			
			var timeNodeStart = System.nanoTime();
			Company.NODE.serialize(NbtParser.INSTANCE, company, nodeNbt);
			var timeNodeEnd = System.nanoTime();
			
			var timeCodecStart = System.nanoTime();
			Company.CODEC.encode(company, NbtOps.INSTANCE, codecNbt);
			var timeCodecEnd = System.nanoTime();
			
			nodeTime += timeNodeEnd - timeNodeStart;
			codecTime += timeCodecEnd - timeCodecStart;
			
			assert nodeNbt.equals(codecNbt);
		}
		
		System.out.println("Node time: " + nodeTime / 2_500_000.0D / 1_000.0D + "us");
		System.out.println("Codec time: " + codecTime / 2_500_000.0D / 1_000.0D + "us");
		
		System.out.println("Tests succeeded!");
	}
}
