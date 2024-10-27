package com.example.eato

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.eato.model.Recipe
import com.example.eato.model.RecipeDataSource
import com.example.eato.ui.theme.EatOTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EatOTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    EatoApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EatoApp() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                {
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Image(
                            modifier = Modifier
                                .size(50.dp)
                            .padding(4.dp),
                        painter = painterResource(R.drawable.ic_ingredient),
                        contentDescription = null)
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.displayLarge
                        )
                    }
                }
            )
        }
    ){
        val recipes = RecipeDataSource.recipe
        RecipeList(recipes = recipes, contentPadding = it)
    }
}

@Composable
fun RecipeList(
    recipes: List<Recipe>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
){
    LazyColumn(contentPadding = contentPadding) {
        itemsIndexed(recipes){ _, recipe ->
            RecipeItem(
                recipe = recipe,
                modifier = modifier.padding(8.dp)
            )

        }
    }
}

@Composable
fun RecipeItem(
    recipe: Recipe,
    modifier: Modifier = Modifier
){
    var expandedIngredient by remember { mutableStateOf(false) }
    var expandedInstruction by remember { mutableStateOf(false) }
    val colorInstruction by animateColorAsState(
        targetValue = if (expandedInstruction or expandedIngredient) MaterialTheme.colorScheme.tertiaryContainer
        else MaterialTheme.colorScheme.primaryContainer
    )

    Card(modifier = modifier)
    {
        Column(modifier = Modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
            .background(color = colorInstruction)
            .padding(8.dp))

        {
            Text(
                text = stringResource(recipe.dayRes),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.padding(4.dp))
            Image(
                modifier = modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(MaterialTheme.shapes.small)
                    .shadow(elevation = 4.dp, spotColor = Color.Red, shape = RoundedCornerShape(2.dp)),
                contentScale = ContentScale.Crop,
                painter = painterResource(recipe.imageRes),
                contentDescription = null,
            )
            Spacer(Modifier.padding(4.dp))
            Text(
                text = stringResource(recipe.nameRes),
                style = MaterialTheme.typography.titleLarge
            )
           Row(
               verticalAlignment = Alignment.CenterVertically
           ){
               /*val annotatedString = buildAnnotatedString {
                   appendInlineContent(id = "imageId")
                   append("Ingredients:  ")
               }
               val inlineContentMap = mapOf(
                   "imageId" to InlineTextContent(
                       Placeholder(20.sp, 20.sp, PlaceholderVerticalAlign.TextCenter)
                   ) {
                       Icon(
                           painter = painterResource(R.drawable.ic_ingredient),
                           modifier = Modifier.fillMaxSize(),
                           contentDescription = null
                       )
                   }
               )
               Text(annotatedString, inlineContent = inlineContentMap)*/
//               Icon(
//                   painter = painterResource(R.drawable.ic_ingredient),
//                   contentDescription = null,
//                   tint = MaterialTheme.colorScheme.secondary,
//                   modifier = modifier.size(20.dp),
//               )
               Text(
                   text = "Ingredients: ",
                   style = MaterialTheme.typography.bodyMedium,
               )
               Spacer(Modifier.weight(2f))
               ExtendedButton(
                   expanded = expandedIngredient,
                   onClick = { expandedIngredient = !expandedIngredient })
           }
            if (expandedIngredient){
                Text(
                    text = stringResource(recipe.ingredientRes),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "Instruction: ",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.weight(1f))
                ExtendedButton(
                    expanded = expandedInstruction,
                    onClick = { expandedInstruction = !expandedInstruction })
            }
            if (expandedInstruction){
                Text(
                    text = stringResource(recipe.instructionRes),
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}

@Composable
private fun ExtendedButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
            contentDescription = "Ingredients: ",
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}



@Preview(showBackground = true)
@Composable
fun RecipePreview() {
    val recipe =  Recipe(
        imageRes = R.drawable.acar,
        dayRes = R.string.day1,
        nameRes = R.string.name1,
        ingredientRes = R.string.ingredient1,
        instructionRes = R.string.instruction1
    )
    EatOTheme(darkTheme = false){
        RecipeItem(recipe = recipe)
    }
}

@Preview("Recipe List")
@Composable
fun RecipeListPreview() {
    EatOTheme(darkTheme = false) {
        EatoApp()
    }
}
