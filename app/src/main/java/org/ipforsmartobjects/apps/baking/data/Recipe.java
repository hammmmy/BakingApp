package org.ipforsmartobjects.apps.baking.data;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Recipe implements Parcelable
{

    private int id;
    private String name;
    private List<Ingredient> ingredients = null;
    private List<Step> steps = null;
    private int servings;
    private String image;
    public final static Parcelable.Creator<Recipe> CREATOR = new Creator<Recipe>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Recipe createFromParcel(Parcel in) {
            Recipe instance = new Recipe();
            instance.id = ((int) in.readValue((int.class.getClassLoader())));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.ingredients, (org.ipforsmartobjects.apps.baking.data.Ingredient.class.getClassLoader()));
            in.readList(instance.steps, (org.ipforsmartobjects.apps.baking.data.Step.class.getClassLoader()));
            instance.servings = ((int) in.readValue((int.class.getClassLoader())));
            instance.image = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Recipe[] newArray(int size) {
            return (new Recipe[size]);
        }

    }
            ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeList(ingredients);
        dest.writeList(steps);
        dest.writeValue(servings);
        dest.writeValue(image);
    }

    public int describeContents() {
        return 0;
    }

}