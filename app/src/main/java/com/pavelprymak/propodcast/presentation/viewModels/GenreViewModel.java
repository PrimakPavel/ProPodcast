package com.pavelprymak.propodcast.presentation.viewModels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;

import com.pavelprymak.propodcast.R;
import com.pavelprymak.propodcast.model.network.PodcastApiController;
import com.pavelprymak.propodcast.model.network.pojo.genres.GenresItem;
import com.pavelprymak.propodcast.model.network.repo.PodcastRepoImpl;
import com.pavelprymak.propodcast.model.network.repo.PodcastRepoRx;
import com.pavelprymak.propodcast.presentation.common.StatesBatch;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class GenreViewModel extends AndroidViewModel {
    private final PodcastRepoRx mRepo = new PodcastRepoImpl(PodcastApiController.getInstance().getPodcastApi());
    private final StatesBatch<List<GenresItem>> mGenresDataBatch = new StatesBatch<>();
    private final Context mContext;
    private final GenresItem mGenreAllItem;

    public GenreViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
        mGenreAllItem = createAllGenreItem();
    }

    public StatesBatch<List<GenresItem>> getGenresBatch() {
        if (mGenresDataBatch.getData().getValue() == null) {
            mGenresDataBatch.postLoading(true);
            Disposable disposable = mRepo.getGenres()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(genresResponse -> {
                        List<GenresItem> genres = new ArrayList<>();
                        genres.add(mGenreAllItem);
                        genres.addAll(genresResponse.getGenres());
                        mGenresDataBatch.postData(genres);
                    }, mGenresDataBatch::postError);
        }
        return mGenresDataBatch;
    }

    public void removeObsorvers(LifecycleOwner lifecycleOwner) {
        mGenresDataBatch.getData().removeObservers(lifecycleOwner);
        mGenresDataBatch.getError().removeObservers(lifecycleOwner);
        mGenresDataBatch.getLoading().removeObservers(lifecycleOwner);

    }

    private GenresItem createAllGenreItem() {
        GenresItem genreAll = new GenresItem();
        genreAll.setId(0);
        genreAll.setName(mContext.getString(R.string.genres_all_title));
        return genreAll;
    }
}
